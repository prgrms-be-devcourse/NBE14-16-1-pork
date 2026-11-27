// 커밋 메시지 규칙: "[Jira 티켓]type: 제목" (scope 없는 conventional commits 앞에 Jira 티켓을 붙인다)
// 예) [PORK-12]feat: 상품 목록 조회 화면 추가
const commitlintConfig = {
	extends: ['@commitlint/config-conventional'],
	parserPreset: {
		parserOpts: {
			// [Jira 티켓]은 생략할 수 있다
			headerPattern: /^(?:\[([A-Z][A-Z0-9]+-\d+)\]\s?)?(\w+): (.+)$/,
			headerCorrespondence: ['ticket', 'type', 'subject'],
		},
	},
	plugins: [
		{
			rules: {
				// 헤더 맨 앞에 [Jira 티켓]이 있어야 한다
				'ticket-empty': ({ ticket }) => [
					Boolean(ticket),
					'제목 앞에 Jira 티켓을 붙여 주세요. 예) [PORK-12]feat: 제목',
				],
			},
		},
	],
	rules: {
		// Jira가 준비될 때까지 티켓은 선택이다. 준비되면 0을 2로 바꿔 필수로 한다
		'ticket-empty': [0, 'always'],
		'type-enum': [
			2,
			'always',
			['feat', 'fix', 'refactor', 'style', 'docs', 'test', 'chore', 'build'],
		],
		'scope-empty': [2, 'always'],
		'subject-max-length': [2, 'always', 50],
		// 한글 제목을 쓰므로 대소문자 규칙은 끈다
		'subject-case': [0],
		'header-max-length': [2, 'always', 80],
		'body-max-line-length': [0],
		'footer-max-line-length': [0],
	},
};

export default commitlintConfig;
