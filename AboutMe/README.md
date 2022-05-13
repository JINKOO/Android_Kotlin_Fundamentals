* ## 실행화면
  <img width="250" alt="AboutMe_ScreenShot" src="https://user-images.githubusercontent.com/37657541/167802235-b86f0db7-2687-42d8-b8f5-4890eb45fe55.png">
  <img width="250" alt="AboutMe_Screenshot2" src="https://user-images.githubusercontent.com/37657541/167802649-5725363f-a8a7-4e7c-8c01-d70543f0c491.png">

* ## Android 개념 (feat. DataBinding)
* ### padding vs margin
  - padding : View의 boader와 View의 content과의 간격을 의미한다. 
  - margin : View의 boarder와 상위 Layout의 간격을 의미한다.
  <img width="400" alt="스크린샷 2022-05-13 오후 12 53 27" src="https://user-images.githubusercontent.com/37657541/168207960-e6dfb285-da08-4c1b-beef-4a9b24a617a5.png">


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
  
  
* ### `DataBinding`
  - view가 생성 또는 재생성 될때, Android System은 view 계층에서 runtime 때 해당 view를 찾는 `findViewById()`를 매번 호출해 탐색한다.  
    view의 개수가 적을 때에는 어느 정도 `findViewById()`로 성능을 감수 할 수 있지만,  
    일반 상용 application에는 무수히 많은 view들이 존재한다. 따라서, 성능 저하가 발생하고, 이는 사용 환경에 불편함을 초래    
    가장 좋은 디자인이라도, nested view가 존재 할 수 있다.  
    
    다음과 같은 layout이 있다고 가정해보자.
    ```html
    <LinearLayout>
      android:id="@+id/linearlayout"
      ...
      <ScrollView>
        android:id="@+id/scrollview"
        ...
        <TextView
          android:id="@+id/smaple_text"/>
          ...
      </ScrollView>  
    </LinearLayout>
    ```
    
    
    위와 같이 view 계층이 deep하고 복잡하면, 특정 view의 id값을 찾는 `findViewById()`의 탐색 속도는 시간 소요가 걸리고, 이는 app의 속도를 저하시킨다.
    이는 사용자가 App을 사용하는데 있어 좋지 않는 사용 환경을 초래한다.
     
    해결 방안으로, 우리는 프로퍼티 variable을 사용해서 view에 대한 reference를 cache할 수 있다. 다음과 같이 말이다.  
    (~~Android devleoper 개발자 과정에서 처음에는 `findViewById()`를 지역 변수에 사용했는데...의도가 뭐지..?~~)
    ```kotlin
    class MainActivity : AppCompatActivity {
      
      // 프로퍼티 variable을 통한 view cache
      private lateinit var sampleText: TextView
      ...
      
      override fun onCreate() {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        sampleText = findViewById(R.id.sample_text)
        ...
      }
      ...
    }
    ```
    하지만, 이렇게 하면, view 별로 프로퍼티 변수를 선언하고, `Activity`코드 어디에선가 `findViewById()`를 사용해 초기화를 진행해야한다.
    view들이 무수히 많으면 코드도 길어지고, 할 일 이 많아진다....
    
    이를 해결하기 위한 것이 `DataBinding`!!!
    
    <img width="1239" alt="스크린샷 2022-05-12 오후 10 19 40" src="https://user-images.githubusercontent.com/37657541/168084234-99e17958-e88e-4dc4-9711-bc5b37b5ed7e.png">
    
    각 view의 reference를 포함하는, 어떤 object를 생성하는데, 이 object가 `Binding`object이다.
    `Binding`object 는 생성한 프로젝트의 app전체에서 사용가능하다.
    이러한 기법이 Andoird의 `DataBinding`이다.

    한번 binding object가 생성되면, views, data등 에 binding object를 통해 접근 가능하다.
    `findViewById()`와 같이 view 계층에서 탐색을 runtime때 수행하지 않는다.
    
    ### `DataBinding`이점
    * `findViewById()`를 사용했을 때 보다 Code가 간결해진다.
    * Data와 View가 분리 된다.(추후, 과정에서 이 이점은 빛을 낸다고 한다?)
    * Android System은 view의 reference갖기 위해 **오직 한번**의 탐색을 view 계층에서 수행한다.
      app이 처음 시작 될때 수행한다.
      사용자와 상호작용하는 runtime때 수행하지 않는다.
    * view에 접근하는데 있어, type safety(compiler가 compile 시점에서 타입 체크)를 제공한다. 
    
    
    ### 그래서 어떻게 사용하는데?
    1. `build.gradle (Module: app)`에 다음 코드 추가
    ```gradle
    buildFeatures {
      dataBinding true
    }
    ```
    2. activty_main.xml에서 전체 xml코드를 `<layout>` tag로 감싼다.
    3. 기존 root viewgroup에 존재하던 namespace 정의문을 `<layout>` tag로 옮긴다.
    ```html
    <layout 
      xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto">
    </layout>
    ```
    4. Activity 코드
    ```kotlin
    class MainActivity : AppCompatActivity {
      
      // activity_main + Binding
      private lateinit var binding: ActivityMainBinding
      ...
      
      override fun onCreate() {
        super.onCreate(savedInstanceState)
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        // xml코드의 id가 sample_text이면 --> binding.sampleText형태로 접근 가능
        binding.sampleText.text = "test"
        ...
      }
      ...
    }
    ```
  
