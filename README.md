# home-back

* HOME 내부 API 백엔드에서 제공 및 관리하는 Api 서버입니다.

## Contribution Rules

### Commit Convention

```
type(scope): body
```

#### Commit Type

- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Changes to documentation
- `style`: Formatting, missing semi colons, etc; no code change
- `test`: Adding tests, refactoring test; no production code change

### Branching Strategy

- `master`: branch to manage only stable states deployed to product
- `develop`: branch to integrate features to be deployed (development is mainly based on this
  branch)
- `your-name`: branch to develop new features

#### Branch Flows

- branch `your-name` from `develop` -> develop features in `your-name` -> pull request to
  `develop` -> approve and merge to `develop`
- `develop` become distributable -> merge `develop` to `master`, deploy `master` to product, add a
  version tag to `master`

#### FlyWay

- Flyway는 데이터베이스 스키마 버전 관리 도구입니다.
- SQL 스크립트를 사용하여 데이터베이스 변경사항을 추적하고 적용할 수 있게 해줍니다.
- V, U ,R 중 선택
    - V(Version) : 버전 마이그레이션
    - U(Undo) : 실행취소
    - R(Repeatable) : 반복 가능한 마이그레이션
    - 버전 상관없이 매번 실행되는 스크립트로 버전 명시가 필요없다.
    - 예시로 테스트를 위해 더미데이터를 넣을 때 사용
        - Separator : 구분자로 _ 가 2개 인것에 주의하자 1개로 작성하면 flyway에서 해당 파일을 스킵해버린다.
        - Description : 실질적 파일명으로 밑줄이나 공백으로 단어를 구분한다.
        - Suffix : 접미사로 보통 .sql을 사용한다

  ### FlyWay 참고링크
    - https://mincanit.tistory.com/95
    - https://russell-seo.tistory.com/5
    - https://myeongdev.tistory.com/93
    - https://dkswnkk.tistory.com/762

  ### Google Coding Conversion
    - https://bestinu.tistory.com/64

# 운영환경 배포방법


