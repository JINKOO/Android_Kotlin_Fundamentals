AndroidTrivia - starter code
============================

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
- app 내부에서 화면들 간의 계층에 따라 화면 전환 한다. app 외부로 사용자가 이동 할 수 없다.  cf) Back Button
- Back Button은 시스템 navigation bar 에서 제공하는 버튼으로, 본 app이 실행 되지 않아도 존재하는 버튼
- app 내부에서는 back stack에 의해서 화면 전환이 이루어 진다.

#. `NavigationUI`
-------------------
- navigation component는 NavigationUI라는 UI 라이브러리를 포함한다.
- app bar, navigation drawer, bottom navigation을 관리한다.
- `NavigationUI`class를 사용해서, app bar에 Up Button을 추가할 수 있다.
- startDestination을 제외한 모든 destination에 Up Button이 App bar에 추가 된다.

