<!--
![실행 화면](https://user-images.githubusercontent.com/37657541/167548174-f3c3e07e-d111-49fe-840e-be2dabf0a9e9.png)
-->
* ## 실행 화면
  <img width="250" alt="DiceRollScreen" src="https://user-images.githubusercontent.com/37657541/167548174-f3c3e07e-d111-49fe-840e-be2dabf0a9e9.png">

* ## DiceRoll Project에서의 Android 개념
* ### android:layout_gravity vs android:gravity 속성
  - layout_gravity : 현재 view의 상위 view에 맞게 정렬
  - gravity : 현재 view 내부의 view를 정렬
  
* ### Vector drawable vs Bitmap
  - Vector drawable은 XML형식으로 된 이미지 이다. 
  - PNG 파일과 같은 Bitmap형식의 이미지 보다 용량이 작고, 유연하다.(어떠한 사이즈까지 scale out이 가능) 
 
* ### `findViewById()`
  - 안드로이드 시스템은 프로젝트의 전체 뷰 계층에서 해당 view의 id값을 탐색한다.
  - `findViewById()`의 사용은 1번만 해서 각 view의 변수에 할당해서 반복적인 호출을 하지 않도록 한다.
  - 추후, `DataBinding`으로 대체.
  
* ### lateinit keyword
  ```kotlin
  private lateinit var smapleText: TextView
  ```
  - Kotlin Compiler에게 해당 변수를 사용하기 전에 초기화 할 것임을 약속.
  - lateinit을 사용하지 않으면, nullalbe로 변수를 선언하고, 사용 할 때에는 매번 null check를 해야한다.
  - lateinit을 사용함으로써, non-nullable 타입으로 사용할 수 있다.
  
* ### tools namespace and app namespace
  - tools nampesapce는 Andoird Studio에서 layout Design화면에서만 보인다.
  - 실제 컴파일 할 때에는 제외.
  - app namespace는 Android core framework가 아닌, Custom 코드나 라이브러리의 속성을 사용을 위한 namespace이다.
