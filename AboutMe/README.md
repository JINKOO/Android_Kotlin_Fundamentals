* ## 실행화면
  <img width="250" alt="AboutMe_ScreenShot" src="https://user-images.githubusercontent.com/37657541/167802235-b86f0db7-2687-42d8-b8f5-4890eb45fe55.png">
  <img width="250" alt="AboutMe_Screenshot2" src="https://user-images.githubusercontent.com/37657541/167802649-5725363f-a8a7-4e7c-8c01-d70543f0c491.png">

* ## Android 개념
* ### padding vs margin
  - padding : View의 boader와 View의 content과의 간격을 의미한다. 
  - margin : View의 boarder와 상위 Layout의 간격을 의미한다.

* ### ScrollView
  - ScrollView는 하나의 View 혹은, View Group(보통 LinearLayout)을 가질 수 있다.(자식 View가 여러개인 경우에는 에러 발생)

* ### `getSystemService(name: String)`
  - Context 클래스의 함수
  - 주어진 매개 변수에 대응되는 Android가 제공하는 시스템-레벨 서비스(디바이스나 Android Framework 내 기능을 다른 App과 공유하고자 시스템으로 부터 객체를 얻을 때 사용)를 요청.
  - 이 함수를 사용해 성공적으로 객체를 반환하면, Manager접미사가 붙은 객체 반환.
  ```kotlin
  // INPUT_METHOD_SERVICE를 요청하고, InputMethodManager를 반환
  val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  ```
* ### 하드웨어 키보드 제어 함수
  - InputMethodManager의 함수
  - `showSoftInput(view: View, flags: Int)` : 키보드 보임
  - `hideSoftInputFromWindow(windowToken: IBinber, flags: Int)` : 키보드 숨김
  ``` kotlin
  val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  // 1. eidtText입력을 위한 키보드 보임
  // 입력 대상의 focus가 존재해야하므로 입력대상에 focus요청
  editText.requestFocus()
  inputMethodManager.showSoftInput(editText, 0)
  
  // 2.키보드 숨김
  inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  ```
  
