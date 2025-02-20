# ✅ Node.js 기반으로 React 빌드
FROM node:18

# ✅ 작업 디렉토리 설정
WORKDIR /app

# ✅ package.json과 package-lock.json 복사
COPY package.json ./
COPY package-lock.json ./

# ✅ 의존성 설치
RUN npm install

# ✅ React 소스 코드 복사
COPY . .

# ✅ React 애플리케이션 빌드
RUN npm run build

# Nginx 기반 React 앱 실행
FROM nginx:latest

# React 빌드 결과 복사
COPY build /usr/share/nginx/html

# Nginx 설정 파일 복사
COPY nginx.conf /etc/nginx/nginx.conf

# 80 포트 노출
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]

