AndroidTrivia - starter code
============================
실행 화면
-------
<p float="left">
<img width="200" alt="image" src="https://user-images.githubusercontent.com/37657541/168312200-758f42a1-bc67-4a8c-9b61-d2d07d645cc2.png">
<img width="200" alt="image" src="https://user-images.githubusercontent.com/37657541/168312312-e8a8ac08-eb25-4ebe-ac70-58d070cd3748.png">
<img width="200" alt="image" src="https://user-images.githubusercontent.com/37657541/168312406-92a5ea89-0ca9-45b2-8125-1f17b23d2fd2.png">
<img width="200" alt="image" src="https://user-images.githubusercontent.com/37657541/168312483-73a51ed6-4c8d-4a11-950d-596e1a8adc69.png">
</p>



사용 개념
-------
#.`Fragment`
-----------

`Fragment`는 `Activity`안에서 user interface(UI)의 한 부분.  
Single `Activity`에 multiple의 `Fragment`를 조합해 사용 가능.  
하나의 Fragment를 서로 다른 `Activity`에서 재사용 가능.
`Activity`에서 modular section이라고 생각하면 된다. "sub activity" 정도..??

- `Fragment`는 자신 만의 lifecycle을 가지고, 사용자 input event를 수신 가능.
- `Activity`가 실행 중일 때, `Fragment`를 추가하거나, 삭제 가능.
- `Fragment`는 Kotlin Class에서 정의.
- `Fragmnet`의 UI는 XML layout파일에 정의하다.(이 부분은 Activity와 동일)


Android Trivia 프로젝트는 하나의 `Activity`와 다수의 `Fragment`로 구성

`Fragment`에서는 `onCreateView()`를 통해 UI layout파일을 inflate.
activity의 xml파일에서 `<fragment>`를 사용해 사용하고자 하는 Fragment를 추가
이때, name 속성에 Fragment를 지정.
```html
...
<fragment
  android:id="@+id/title_fragment"
  android:name="com.example.android.navigation.TitleFragment"
  ...
  />
...
```

#. Navigation
-------------
* destination : 사용자가 app내부에서 이동 가능한(도달 할 수 있는) 모든 요소. 예를 들면 `Fragment`
* navigation graph : app내부의 destination들의 집합


#. `NavHostFragment`
--------------------
* navigation graph에서 여러 `Fragment` 중 host 역할을 한다.
* 보통 `NavHostFragment`라고 named 한다.
* 사용자가 navigation graph에서 정의되어 있는 destinations(Fragment) 사이에서 이동할 때,  navigation host fragment가 fragment를 교체한다.  또한, `Fragment`는 back stack을 생성하고 관리
```html
<!-- activity_main.xml -->
<fragment
  android:id="@+id/myNavHostFragment"
  android:name="androidx.navigation.fragment.NavHostFragment"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  app:navGraph="@navigation/navigation"
  app:defaultNavHost="true"/>
```

#. action
---------
- destination간(2개의 `Fragment`간)의 이동 관계를 나타내는 속성
- action ID값이 생성되고(`action_startFragment_to_destinationFragment`), 이 값을 kotlin코드에서 사용.
```kotlin
playButton.setOnClikceListener { view: View ->
  view.findNavController().navigate(R.id.action_startFragment_to_destinationFragment)
}
```

#. Back Stack and System Back Button
------------------------------------
* Android System은 사용자가 app 내에서 새로운 destination으로 이동하면 back stack에 삽입한다.(스택 구조는 뭐 다들 알죠..??)
* 사용자가 System back button을 선택하면, back stack의 top에 위치한 view가 보여진다.
* default로 back stack의 top은 가장 최근에 보여진 view이다.

하지만, 우리는 navigation action의 속성을 통해, back stack을 수정할 수 있다!!!!!

예를 들어, Android Trivia App에서는 `GameWonFragment` 그리고 `GameOverFragment`에서 System Back Button을 선택 했을 때, 이전 화면인(back stack의 top view) 다시 `GameFramgment`로 이동한다.

<img width="974" alt="스크린샷 2022-05-13 오후 11 11 00" src="https://user-images.githubusercontent.com/37657541/168302261-43cb0f24-e755-48ef-acc0-3e31a021eafd.png">

하지만 기획 의도가 `TitleFragment`로 이동해, 다시 Game을 시작하거나, 또는 다른 사용자 액션을 취하도록 하는 것이라고 해보자.
이때, 사용하는 속성이 `popUpTo`, `popUpToInclusive`이다.

* `popUpTo` : 명시한 destination은 back stack에서 pop하지 않고, 명시한 destination 전까지 destination들은 pop한다. 
* `popUpToInclusive` 
  - true : back stack의 destination들을 pop하고, 명시한 destination도 back stack에서 pop.
  - false : 명시한 destination은 back stack에서 pop하지 않고, 명시한 destination 전까지 destination들은 pop한다.
* `popUptoInclusive`속성을 지정하지 않는 경우에는 false값일 때와 동일하게 동작.
```html
...
<fragment
        android:id="@+id/gameFragment"
        android:name="com.example.android.navigation.GameFragment"
        android:label="GameFragment"
        tools:layout="@layout/fragment_game" >
        <action
            android:id="@+id/action_gameFragment_to_gameWonFragment"
            app:destination="@id/gameWonFragment"
            app:popUpTo="@id/gameFragment"
            app:popUpToInclusive="true" />

        <action
            android:id="@+id/action_gameFragment_to_gameOverFragment"
            app:destination="@id/gameOverFragment"
            app:popUpTo="@id/titleFragment"
            app:popUpToInclusive="false"/>
    </fragment>
...
```

#. Up Button
------------
- App bar(action bar)에 존재하는 버튼이다. 
- app 내부에서 화면들 간의 계층에 따라 화면 전환 한다. app 외부로 사용자가 이동 할 수 없다.
- cf) Back Button
- Back Button은 시스템 navigation bar 에서 제공하는 버튼으로, 본 app이 실행 되지 않아도 존재하는 버튼
- app 내부에서는 back stack에 의해서 화면 전환이 이루어 진다.

#. `NavigationUI`
-------------------
- navigation component는 NavigationUI라는 UI 라이브러리를 포함한다.
- app bar, navigation drawer, bottom navigation을 관리한다.
- `NavigationUI`class를 사용해서, app bar에 Up Button을 추가할 수 있다.
- startDestination을 제외한 모든 destination에 Up Button이 App bar에 추가 된다.
- navigation controller를 사용해서 app에 사용해 Up Button을 추가한다.
  ```kotlin
  // MainActivity
  override fun onCreate(savedInstanceState: Bundle?) {
    // 1. navigation controller를 찾는다.
    val navController = this.findNavController(R.id.myNavHostFragment)
  
    // 2. app bar와 nav controller를 연결
    NavigationUI.setupActionBarWithNavController(this, navController)
  }
  
  // 3. 해당 함수 override
  override fun onSupportNavigateUp(): Boolean {
    val navController = this.findNavController(R.id.myNavHostFragment)
    return navController.navigateUp()
  }
  ```
- 해당 코드를 실행하면, Up Button은 startDestination 여기서는(`TitleFragment`)를 제외하고 모든 destination의 App bar에 나타난다.
- 어떤 화면에서든지, Up Button을 클릭하면, start destination으로 돌아온다. 여기서는 `TitleFragment`로 돌아온다.


#. Options Menu
---------------
- app bar에 나타나는 3개의 점이 vertical하게 디자인 되어있는 메뉴 버튼.
- 사용자는 해당 이 부분을 tap하여, 특정 menu화면(destination)으로 이동할 수 있다. 여기서는 `AboutFragment`
- menu폴더에서 `options_menu.xml` 형식의 xml파일을 생성 
- menu Item을 추가할 때, ID값을 navigation graph에서 이동하고자 하는 `Fragment`의 ID와 동일하게 한다. (`onClick` handler를 구현하지 않아도 된다.)
- `Activity`의 `onCreate()` 또는 `Fragment`의 `onCreatView()`에서 `setHasOptionsMenu(true)`의 코드를 추가
- `onCreateOptionsMenu()`, `onOptionsItemSelected()`를 override한다.
  ```kotlin
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    // 우리가 생성한 menu xml파일
    inflater.inflate(R.menu.options_menu, menu)
  }
  
  // options menu에서 사용자가 선택한 menu에 따라 적절한 action을 한다.
  // navigation graph에서 option menu를 선택했을 때 이동하는 destination의 ID와, menu Item의 ID가 같기 때문에, onClick listener 구현하지 않음.
  override fun onOptionsItemSelected(item: MenuItem): Booelan {
    return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) 
                        || super.onOptionsItemSelected(item)
  }
  ```

#. navigation drawer
--------------------
- 화면 edge에서 slide되어 나오는 panel.
- drawer는 header와 menu로 구성
- 사용자가 화면을 left --> right로 쓸거나, `nav drawer button` 또는 `hamburger` icon을 선택해 drawer를 실행한다.
- drawer 버튼은 app bar에 존재하며, 사용자가 start destination 있을 때 화면에 나타난다.
- Material Components for Android, `Material` 라이브러리의 한 종류이다. (dependency를 추가해야 한다.)

- res에서 drawer menu xml을 생성하고, 해당 menu.xml에서 menu item을 추가한다.
- navigation graph에서 사용자가 drawer에서 선택한 menu item 따라 이동할 destination(`Fragment`)을 추가(menu item의 ID값과 동일하면 좋다)
- `activity_main.xml`에 `DrawerLayout`추가
  ```html
  <layout xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto">
    <androidx.drawerlayout.widget.DrawerLayout
       android:id="@+id/drawerLayout"
       android:layout_width="match_parent"
       android:layout_height="match_parent">

    <LinearLayout
       . . . 
    </LinearLayout>
      
    <com.google.android.material.navigation.NavigationView
      android:id="@+id/navView"
      android:layout_width="wrap_content"
      android:layout_height="match_parent"
      android:layout_gravity="start"
      app:headerLayout="@layout/nav_header"
      app:menu="@menu/navdrawer_menu" />  
    </androidx.drawerlayout.widget.DrawerLayout>
  </layout>
  ```
  
 - `MainActivity`에서 drawerLayout과 navigation conroller를 연결해야한다.(Up Button처럼) 그래야, 사용자가 navigation drawer에서 menu item을 선택 했을 때,  
   해당 destination으로 이동하기 때문
   ```kotlin
   override fun onCreate(savedInstanceState: Bundle?) {
    ...
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    
    // navigation drawer와 nav controller를 연결한다.   
    NavigationUI.setUpWithNavController(binding.navView, navController)
    
    // 햄버거 버튼과, Up Button을 사용할 수 있도록 한다.
    NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
   }
   
   // 
   override fun onSupportNavigateUp(): Boolean {
    val navController = this.findNavController(R.id.myNavHostFragment)
    return NavigationUI.navigateUp(navController, drawerLayout)
   }
   ```

#. Safe Args
------------
- Gradle plugin으로, Fragement간 argument를 전달 할 때 사용하며, 이 transaction이 수행할 때 type-safe를 통해 발생하는 bug를 예방한다.  
  compile-time때, error를 탐지 할 수 있도록 어느 특정 code와 class 파일들을 생성한다.
- plugin이 생성한 `NavDirection`class를 사용해서 fragment간 argument를 전달한다.
- 왜 사용하는 가??
  App 내부에서 하나의 `Fragment`가 다른 `Fragment`로 data를 전달 해야하는 경우가 있다.  
  기존에 우리가 사용하던 한 가지 방법은 key-value쌍의 `Bundle` Class의 instance를 사용하는 것이다.  
  
  예를 들어, A Fragment --> B Fragment로 `Bundle` instance를 전달해야 할때,  
  Fragment A는 bundle을 생성하고, key-value쌍으로 data를 저장 한 후, 이 `Bundle`을 Fragment B로 전달한다.
  Fragment B에서는 key값으로, `Bundle`에서 key-value를 가져온다.  
  
  Bundle을 사용하는 경우에는, compile-time때 문제가 발생하지 않지만, run time때 다음 2가지의 error가 발생 할 수 있다.
  
  * Type mismatch error : Fragment A --> Fragment B로 String을 보냈지만, Fragment B는 `Bundle`에 Integer를 요청해 default값인 0을 가지게 된다면,  
    0은 유효한 값이므로, compile-time때 어떠한 error가 발생했는지 모른다. run time때, 올바르지 않게 동작하거나, app이 죽을 수 있다.
   
  * Missing Key Error : Fragment B가 `Bundle`에 존재하지 않는 argument를 요청하면, `null`을 리턴한다. 이 경우 역시 compile time때는 catch할 수 없다.  
    하지만, null로 인해, run time때 사용자에게 심각한 문제를 초래할 수 있다.
  
  위의 2가지 경우를 보면, 모두 run time때 error가 발생한다.  
  Safe Args를 사용해서 이를 compile time때 error를 잡을 수 있다.
 
- `NavDirection` class들이 생성되는데, 각 fragment마다 생성된다. `TitleFragment`는 `TitleFragmentDirection` class가 생성된다.
- safe args를 사용하려면 기존의 action ID값으로, `Navcontroller.navigate()`를 사용했지만, `NavDirection` Class를 사용해야 한다.
  ```kotlin
  view.findNavController().navigate(GameFragmentDirection.actionGameFragmentToGameWonFragment(numQuestions, numCorrect))
  ```
  
- `GameFragment` -> `GameWonFragment`로의 argument전달이 필요한 경우, `GameWonFragment`에 Argument를 추가한다.
  ```html
  <fragment
        android:id="@+id/gameWonFragment"
        android:name="com.example.android.navigation.GameWonFragment"
        android:label="fragment_game_won"
        tools:layout="@layout/fragment_game_won" >
        <action
            android:id="@+id/action_gameWonFragment_to_gameFragment"
            app:destination="@id/gameFragment"
            app:popUpTo="@id/titleFragment" />
        <argument
            android:name="numQuestions"
            app:argType="integer" />
        <argument
            android:name="numCorrect"
            app:argType="integer" />
  </fragment>
  ```
  
- 'GameWonFragment'에서의 onCreateView()에서 다음과 같이 사용한다.
  ```kotlin
  val GameWonFragmentArgs.fromBundle(requireArguments())
  Log.d(TAG, "args :: ${args.numQuestions}, ${args.numCorrect}"
  ```

#. Implicit Intent
------------------
- Android는 다른 app의 Activity로 이동 가능한데, 이를 가능하게 해주는 것이 `Intent`이다.
- `Intent`는 Android의 컴포넌트 간 통신을 할 때 사용하는 간단한 messaging object.
- explicit intent를 사용해서 특정 Activity로 이동할 수 있다.
- implicit intent의 경우에는 Android System이 누가 해당 intent를 처리할 지 모르기 때문에, 해당 intent를 실행할 수 있는, device에 존재하는 app들을 사용자가 선택하게 한다.  
  예를 들면, 공유하기 기능 (`Intent.ACTION_SEND`)일 때, device의 공유가 가능한 모든 app들이 대화상자에 표시되고(카톡, 메일) 사용자가 선택 할 수 있다. 
  
