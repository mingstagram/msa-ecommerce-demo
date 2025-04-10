# 🚀 MSA 기반 eCommerce 플랫폼

## 📌 프로젝트 개요

이 프로젝트는 MSA(Microservice Architecture) 기반의 eCommerce 플랫폼을 구현하는 데모 프로젝트입니다. 현재 기본적인 MSA 구조를 갖추고 있으며, 지속적으로 기능을 추가하고 있습니다.

## ✅ 주요 목표

- MSA 아키텍처 구현
- 서비스 간 통신 구현
- 분산 시스템 설계
- 마이크로서비스 운영 경험

## 📌 기술 스택

### Backend

- **Framework**: Spring Boot 3.2.x
- **Service Discovery**: Eureka Client
- **Database**: MariaDB
- **ORM**: Spring Data JPA
- **Build Tool**: Maven

### 공통 라이브러리

- **Lombok**: 코드 간소화
- **ModelMapper**: 객체 매핑
- **Validation**: Bean Validation

## 📌 서비스 구성

### 1. User Service

- 사용자 관리 기본 구조
- JPA 엔티티 구성
- 기본적인 REST API 엔드포인트 (회원가입, 조회)
- 환경 설정 관리 (health check, welcome)

### 2. Catalog Service

- 상품 관리 기본 구조
- JPA 엔티티 구성
- 기본적인 REST API 엔드포인트 (상품 조회)
- 환경 설정 관리 (health check)

### 3. Order Service

- 주문 관리 기본 구조
- JPA 엔티티 구성
- 기본적인 REST API 엔드포인트 (주문 생성, 조회)
- Kafka 연동 (주문 이벤트 발행)
- 환경 설정 관리 (health check)

## 📌 개발 예정 기능

- JWT 기반 인증/인가
- OpenFeign을 이용한 서비스 간 통신
- Docker 컨테이너화
- 모니터링 및 관리 기능
- 상품 재고 관리
- 주문 상태 관리
- 결제 시스템 연동

## 📌 Contact

- **이름**: 김민국
- **이메일**: amgkim21@gmail.com
- **GitHub**: [github.com/mingstagram](https://github.com/mingstagram)
- **Tech Blog**: [mingucci.tistory.com](https://mingucci.tistory.com)
