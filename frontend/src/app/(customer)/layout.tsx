// 고객 페이지 공통 레이아웃: (customer) 라우트 그룹은 URL에 나타나지 않으므로 고객 화면은 "/"부터 시작한다
export default function CustomerLayout({ children }: LayoutProps<'/'>) {
	return (
		<div className='flex min-h-full flex-1 flex-col'>
			<header className='border-b border-divider bg-bg-surface'>
				<div className='mx-auto flex h-(--size-control-lg) w-full max-w-(--size-screen) items-center px-xl'>
					<span className='text-heading-h3'>Grids &amp; Circles</span>
				</div>
			</header>
			<main className='mx-auto w-full max-w-(--size-screen) flex-1 px-xl py-2xl'>{children}</main>
		</div>
	);
}
