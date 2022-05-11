* ## 실행화면
  <img width="250" alt="스크린샷 2022-05-11 오후 11 37 42" src="https://user-images.githubusercontent.com/37657541/167876538-f485753d-366e-4ea8-859e-98ef9c4eca8b.png">
  <img width="250" alt="스크린샷 2022-05-11 오후 11 38 10" src="https://user-images.githubusercontent.com/37657541/167876546-c496a9f2-5242-4e3c-91d5-5f01068aeeba.png">

* ## 사용 Android 개념
* ### `ConstraintLayout`
  - `ViewGroup`으로 자식 view의 위치와 사이즈를 유연하게 대처 할 수 있는 layout.
  - 각 자식 view는 적어도 하나의 horizontal, vertical constraint를 가져야 한다.
  - parent, guideline, 다른 view 등에 constraints를 추가하며 위치를 지정.
  - `LinearLayout`에 비해 뷰 계층이 deep하지 않다. flatter하다.

* ### Chain
  - 양방향 constraint를 설정해 view들을 연결.
  - vertical, horizontal모두 가능
 
* ### Baseline constraints
  - view의 text baseline을 text가 존재하는 다른 view 정렬.
  - view들이 서로 다른 font size를 가지고 있을 때 용이.

* ### Design-time attributes
  - `tools` namespace
  - ex) `tools:text = "Hello World"`
