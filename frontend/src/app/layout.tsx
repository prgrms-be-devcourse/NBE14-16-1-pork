import type { Metadata } from 'next';
import { Noto_Sans_KR } from 'next/font/google';
import './globals.css';

// 디자인 토큰 font.family.base(Noto Sans KR)를 next/font로 내려받아 --font-noto-sans-kr로 노출한다
const notoSansKr = Noto_Sans_KR({
	variable: '--font-noto-sans-kr',
	subsets: ['latin'],
	weight: ['400', '500', '700'],
});

export const metadata: Metadata = {
	title: 'Grids & Circles',
	description: '로컬 카페 Grids & Circles의 커피 원두 주문 서비스',
};

export default function RootLayout({ children }: LayoutProps<'/'>) {
	return (
		<html lang='ko' className={`${notoSansKr.variable} h-full antialiased`}>
			<body className='min-h-full flex flex-col'>{children}</body>
		</html>
	);
}
