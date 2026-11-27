// 관리자 페이지 공통 레이아웃: /admin 아래 모든 화면에 사이드바와 본문 영역을 적용한다
export default function AdminLayout({ children }: LayoutProps<'/admin'>) {
	return (
		<div className='flex min-h-full flex-1'>
			<aside className='w-(--size-sidebar) shrink-0 bg-bg-inverse p-xl text-text-inverse'>
				<span className='text-heading-h3'>관리자</span>
			</aside>
			<main className='flex-1 px-2xl py-2xl'>{children}</main>
		</div>
	);
}
