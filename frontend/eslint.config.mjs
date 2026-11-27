import { defineConfig, globalIgnores } from 'eslint/config';
import nextVitals from 'eslint-config-next/core-web-vitals';
import nextTs from 'eslint-config-next/typescript';
import prettier from 'eslint-config-prettier/flat';

const eslintConfig = defineConfig([
	...nextVitals,
	...nextTs,
	{
		// shadcn이 만드는 컴포넌트는 cn 패키지를 직접 import한다. 토큰 설정이 들어간 @/lib/utils를 쓰게 한다
		ignores: ['src/lib/utils.ts'],
		rules: {
			'no-restricted-imports': [
				'error',
				{
					paths: [
						{
							name: 'cn',
							importNames: ['cn'],
							message: "디자인 토큰 설정이 들어간 cn을 쓰도록 '@/lib/utils'에서 import 하세요.",
						},
					],
				},
			],
		},
	},
	// Prettier와 겹치는 포맷 관련 룰은 끈다 (항상 마지막에 둔다)
	prettier,
	globalIgnores(['.next/**', 'out/**', 'build/**', 'next-env.d.ts']),
]);

export default eslintConfig;
