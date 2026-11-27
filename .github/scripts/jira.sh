#!/usr/bin/env bash
# Jira REST API(v3) 호출 헬퍼
# 사용법:
#   jira.sh transition <이슈 키> <상태 이름>   예) jira.sh transition PORK-12 "진행 중"
#   jira.sh comment <이슈 키> <문구> [링크]    예) jira.sh comment PORK-12 "PR 생성: #3" https://github.com/...
#   jira.sh issue-card <이슈 키>               예) jira.sh issue-card PORK-12  (PR 댓글용 마크다운 출력)
# 필요한 환경변수: JIRA_BASE_URL, JIRA_USER_EMAIL, JIRA_API_TOKEN
set -euo pipefail

: "${JIRA_BASE_URL:?JIRA_BASE_URL 시크릿이 필요합니다}"
: "${JIRA_USER_EMAIL:?JIRA_USER_EMAIL 시크릿이 필요합니다}"
: "${JIRA_API_TOKEN:?JIRA_API_TOKEN 시크릿이 필요합니다}"

BASE_URL="${JIRA_BASE_URL%/}"

# Jira API를 호출하고 응답 본문을 출력한다. 2xx가 아니면 실패한다.
jira_api() {
	local method="$1" path="$2" data="${3:-}"
	local response status body

	response=$(curl --silent --show-error \
		--user "${JIRA_USER_EMAIL}:${JIRA_API_TOKEN}" \
		--header 'Accept: application/json' \
		--header 'Content-Type: application/json' \
		--request "$method" \
		${data:+--data "$data"} \
		--write-out '\n%{http_code}' \
		"${BASE_URL}/rest/api/3${path}")

	status="${response##*$'\n'}"
	body="${response%$'\n'*}"

	if [[ "$status" != 2* ]]; then
		echo "::error::Jira API ${method} ${path} 실패 (HTTP ${status}): ${body}" >&2
		return 1
	fi
	printf '%s' "$body"
}

# 이슈를 지정한 상태로 옮긴다. 이미 그 상태면 아무것도 하지 않는다.
transition() {
	local key="$1" target="$2"
	local current transitions transition_id

	current=$(jira_api GET "/issue/${key}?fields=status" | jq -r '.fields.status.name')
	if [[ "$current" == "$target" ]]; then
		echo "${key}: 이미 '${target}' 상태입니다."
		return 0
	fi

	transitions=$(jira_api GET "/issue/${key}/transitions")
	transition_id=$(jq -r --arg target "$target" \
		'[.transitions[] | select(.to.name == $target or .name == $target)][0].id // empty' \
		<<<"$transitions")

	if [[ -z "$transition_id" ]]; then
		local available
		available=$(jq -r '[.transitions[].to.name] | join(", ")' <<<"$transitions")
		echo "::error::${key}: '${current}' → '${target}' 전환을 찾을 수 없습니다. 가능한 상태: ${available}" >&2
		return 1
	fi

	jira_api POST "/issue/${key}/transitions" \
		"$(jq -n --arg id "$transition_id" '{transition: {id: $id}}')" >/dev/null
	echo "${key}: '${current}' → '${target}'"
}

# 이슈에 댓글을 남긴다. 링크를 주면 문구 뒤에 링크를 붙인다.
comment() {
	local key="$1" text="$2" url="${3:-}"
	local payload

	payload=$(jq -n --arg text "$text" --arg url "$url" '{
		body: {
			type: "doc",
			version: 1,
			content: [{
				type: "paragraph",
				content: (
					[{type: "text", text: $text}]
					+ (if $url == "" then [] else
						[{type: "text", text: " "},
						 {type: "text", text: $url, marks: [{type: "link", attrs: {href: $url}}]}]
					end)
				)
			}]
		}
	}')

	jira_api POST "/issue/${key}/comment" "$payload" >/dev/null
	echo "${key}: 댓글 작성 완료"
}

# PR 댓글에 붙일 이슈 카드(마크다운)를 출력한다
issue_card() {
	local key="$1"
	local fields="summary,status,issuetype,assignee,priority,duedate"

	jira_api GET "/issue/${key}?fields=${fields}" | jq -r --arg key "$key" --arg base "$BASE_URL" '
		def cell: tostring | gsub("\\|"; "\\|") | gsub("\n"; " ");
		.fields as $f
		| [
			"<!-- jira-issue-card -->",
			"### 🎫 Jira [\($key)](\($base)/browse/\($key))",
			"",
			"| 항목 | 내용 |",
			"| --- | --- |",
			"| 제목 | \($f.summary // "-" | cell) |",
			"| 유형 | \($f.issuetype.name // "-" | cell) |",
			"| 상태 | \($f.status.name // "-" | cell) |",
			"| 담당자 | \($f.assignee.displayName // "미지정" | cell) |",
			"| 우선순위 | \($f.priority.name // "-" | cell) |",
			"| 기한 | \($f.duedate // "-" | cell) |",
			"",
			"<sub>PR 생성·수정 시점의 Jira 정보입니다. 최신 상태는 링크에서 확인하세요.</sub>"
		]
		| join("\n")'
}

command="${1:-}"
shift || true
case "$command" in
	transition) transition "$@" ;;
	comment) comment "$@" ;;
	issue-card) issue_card "$@" ;;
	*)
		echo "사용법: $0 {transition|comment|issue-card} ..." >&2
		exit 2
		;;
esac
