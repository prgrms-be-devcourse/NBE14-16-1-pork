import * as React from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { Slot } from 'radix-ui';

import { cn } from '@/lib/utils';

// 크기·모서리·글꼴·색상은 모두 디자인 토큰(src/styles/tokens.css)을 쓴다
const buttonVariants = cva(
	"inline-flex shrink-0 items-center justify-center rounded-md border border-transparent bg-clip-padding whitespace-nowrap transition-all outline-none select-none focus-visible:border-ring focus-visible:ring-3 focus-visible:ring-ring/50 disabled:pointer-events-none disabled:opacity-50 aria-invalid:border-destructive aria-invalid:ring-3 aria-invalid:ring-destructive/20 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
	{
		variants: {
			variant: {
				// 주요 버튼
				default: 'bg-primary text-primary-foreground hover:bg-primary/80',
				// 보조 버튼
				outline: 'border-border-strong bg-bg-surface text-text-primary hover:bg-bg-subtle',
				secondary: 'bg-secondary text-secondary-foreground hover:bg-bg-subtle',
				ghost: 'text-text-primary hover:bg-bg-muted',
				// 위험 버튼 (삭제·취소)
				destructive:
					'border-border-danger bg-bg-surface text-text-danger hover:bg-bg-subtle focus-visible:border-border-danger focus-visible:ring-destructive/20',
				link: 'text-text-primary underline-offset-4 hover:underline',
			},
			size: {
				// 기본 버튼 (최소 터치 영역 44px)
				default: 'h-(--size-control-md) gap-xs px-md text-label-button',
				// 작은 버튼·필터 36px
				sm: 'h-(--size-control-sm) gap-2xs px-sm text-label-button',
				// 결제 등 주요 CTA 52px
				lg: 'h-(--size-control-lg) gap-xs px-xl text-label-button-large',
				icon: 'size-(--size-control-md)',
				'icon-sm': 'size-(--size-control-sm)',
				'icon-lg': 'size-(--size-control-lg)',
			},
		},
		compoundVariants: [
			// 주요 버튼은 굵은 글꼴(label.button-strong)을 쓴다. lg는 button-large가 이미 굵다
			{ variant: 'default', size: ['default', 'sm'], className: 'text-label-button-strong' },
		],
		defaultVariants: {
			variant: 'default',
			size: 'default',
		},
	}
);

function Button({
	className,
	variant = 'default',
	size = 'default',
	asChild = false,
	...props
}: React.ComponentProps<'button'> &
	VariantProps<typeof buttonVariants> & {
		asChild?: boolean;
	}) {
	const Comp = asChild ? Slot.Root : 'button';

	return (
		<Comp
			data-slot='button'
			data-variant={variant}
			data-size={size}
			className={cn(buttonVariants({ variant, size, className }))}
			{...props}
		/>
	);
}

export { Button, buttonVariants };
