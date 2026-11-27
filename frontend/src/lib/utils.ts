import { createCn } from 'cn/config';

// 디자인 토큰의 타이포그래피 유틸리티(text-heading-h1 등) 이름. src/styles/tokens.css의 --text-* 와 같다
const typographyTokens = [
	'heading-h1',
	'heading-h2',
	'heading-h3',
	'heading-h4',
	'body-large',
	'body-medium',
	'body-medium-strong',
	'body-small',
	'caption',
	'label-badge',
	'label-button',
	'label-button-strong',
	'label-button-large',
];

// 클래스 병합 함수. 타이포그래피 토큰을 글자 크기 그룹으로 알려 주지 않으면
// text-label-button 같은 클래스를 글자 색으로 오인해 text-primary-foreground 등을 지운다
export const cn = createCn({
	extend: { classGroups: { 'font-size': [{ text: typographyTokens }] } },
});
