// 커밋 시 스테이징된 파일에 실행할 작업을 파트별로 정의한다
// lint-staged는 레포 루트에서 명령을 실행하므로 각 파트의 도구는 --dir로 해당 디렉토리에서 실행한다
const lintStagedConfig = {
	// frontend
	'frontend/**/*.{js,jsx,mjs,cjs,ts,tsx}': [
		'pnpm --dir frontend exec eslint --fix --max-warnings 0',
		'pnpm --dir frontend exec prettier --write',
	],
	'frontend/**/*.{json,css,md,yml,yaml}': 'pnpm --dir frontend exec prettier --write',

	// backend: 포맷터·린터가 정해지면 'backend/**/*.java' 등의 규칙을 추가한다
};

export default lintStagedConfig;
