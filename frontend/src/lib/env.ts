// 환경변수는 이 파일에서만 읽고, 다른 코드는 여기서 내보낸 상수를 쓴다

// 백엔드 API 서버 주소. NEXT_PUBLIC_ 접두사라 빌드할 때 브라우저 코드에도 값이 들어간다
// 값이 없으면 로컬 백엔드(backend/application.yml의 server.port)를 쓴다. 배포 환경에서는 반드시 설정한다
export const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL ?? 'http://localhost:8080';
