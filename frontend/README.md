# Frontend

## 기술 스택

- Next.js 16 (App Router, `src/` 디렉토리)
- TypeScript
- Tailwind CSS 4
- shadcn/ui (`components.json`, 컴포넌트는 `src/components/ui`)
- ESLint (`eslint-config-next`의 core-web-vitals + typescript) + Prettier

## 시작하기

Node.js 24, pnpm 12가 필요합니다.

```bash
pnpm install   # 레포 루트에서 한 번: git 훅(husky) 등록
cd frontend
pnpm install
pnpm dev       # http://localhost:3000
```

## 스크립트

| 명령                | 설명                      |
| ------------------- | ------------------------- |
| `pnpm dev`          | 개발 서버                 |
| `pnpm build`        | 프로덕션 빌드             |
| `pnpm lint`         | ESLint 검사 (경고도 실패) |
| `pnpm lint:fix`     | ESLint 자동 수정          |
| `pnpm format`       | Prettier로 전체 포맷      |
| `pnpm format:check` | 포맷 검사                 |
| `pnpm typecheck`    | 타입 검사                 |

shadcn/ui 컴포넌트 추가: `pnpm dlx shadcn@latest add <컴포넌트>`

## 라우팅

고객 페이지와 관리자 페이지를 라우트로 나눕니다.

| 경로       | 위치                  | 레이아웃                             |
| ---------- | --------------------- | ------------------------------------ |
| `/`, ...   | `src/app/(customer)/` | 고객 헤더 (`(customer)/layout.tsx`)  |
| `/admin/*` | `src/app/admin/`      | 관리자 사이드바 (`admin/layout.tsx`) |

`(customer)`는 라우트 그룹이라 URL에 나타나지 않습니다. 고객 화면은 `(customer)` 아래에, 관리자 화면은 `admin` 아래에 폴더를 만들면 각 레이아웃이 자동으로 적용됩니다. `(customer)` 아래에 `admin` 폴더를 만들면 `/admin`과 경로가 겹쳐 오류가 나니 주의하세요.

## 디자인 토큰

Figma에서 추출한 디자인 토큰은 [`src/styles/tokens.css`](src/styles/tokens.css)에 있습니다. `@theme`에 선언되어 있어 Tailwind 유틸리티로 바로 쓸 수 있습니다.

| 토큰                    | 사용 예                                                           |
| ----------------------- | ----------------------------------------------------------------- |
| 색상 `--color-bg-*` 등  | `bg-bg-surface`, `text-text-secondary`, `border-border-default`   |
| 간격 `--spacing-*`      | `p-md`, `gap-xs`, `mt-xl`                                         |
| 모서리 `--radius-*`     | `rounded-md`, `rounded-full`                                      |
| 타이포그래피 `--text-*` | `text-heading-h2`, `text-body-medium`, `text-label-button-strong` |
| 크기 `--size-*`         | `h-(--size-control-md)`, `w-(--size-sidebar)`                     |

shadcn/ui의 시맨틱 색상(`bg-primary`, `text-muted-foreground` 등)도 [`globals.css`](src/app/globals.css)에서 이 토큰에 연결되어 있습니다.

## 린트와 포맷 자동 실행

- **커밋할 때**: pre-commit 훅이 스테이징된 frontend 파일에 `eslint --fix`와 `prettier --write`를 실행합니다.
- **커밋 메시지**: commit-msg 훅이 commitlint로 `[Jira 티켓]type: 제목` 규칙을 검사합니다. Jira가 준비되기 전까지 티켓은 생략할 수 있습니다.
- **에디터**: 사용하는 에디터의 ESLint, Prettier 연동을 켜면 `frontend`의 설정 파일을 그대로 읽습니다.
- **PR**: `frontend/**`가 바뀐 PR에서 GitHub Actions가 포맷 검사, 린트, 타입 검사, 빌드를 실행합니다.

브랜치, 커밋, PR 규칙은 [CONTRIBUTING.md](../.github/CONTRIBUTING.md)를 참고하세요.
