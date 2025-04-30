# Start Generation Here

## EC2 인스턴스에 올릴 경우 주의사항

AWS EC2 인스턴스에 애플리케이션을 배포할 때 다음 사항을 유의하시기 바랍니다:

1. **보안 그룹 설정**:
    - 인스턴스에 접근할 수 있는 IP의 종류를 IPV4 와 IPV6 두 가지 모두 설정해야 합니다.
    - 나머지 (HTTP, HTTPS, SSH 등) 포트도 모두 열어두어야 합니다.


2. **네이버 개발자 센터 설정 (application.properties)**:
    - 네이버 개발자 센터에서 PUBLIC DNS를 설정할 때 포트 번호는 생략합니다.
    - CallBack URL 은 네이버 개발자 센터에서 설정한 PUBLIC DNS 와 동일하게 설정해야 합니다. 이때 포트 번호를 써야합니다.
    - client id 와 client secret 을 모두 잊지 말고 설정해야합니다.
    - redirect url 은 네이버 개발자 센터에서 설정한 PUBLIC DNS 와 동일하게 설정해야 합니다. 이때 포트 번호를 써야합니다.


이러한 주의사항을 준수하여 EC2 인스턴스에서 안정적이고 안전한 애플리케이션 운영을 보장하세요.


## 환경 설정

[아래의 환경 변수를 각각 설정하면 됩니다]
네이버 클라이언트 ID: NAVER_CLIENT_ID
네이버 클라이언트 시크릿: NAVER_CLIENT_SECRET
네이버 콜백 URL: NAVER_REDIRECT_URI


# End Generation Here
