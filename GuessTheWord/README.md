실행화면
======

<img width="1109" alt="image" src="https://user-images.githubusercontent.com/37657541/170930570-0a1721f6-e16c-49a3-8924-5f15e098bb08.png">

- Challenge 화면
<img width="289" alt="image" src="https://user-images.githubusercontent.com/37657541/170930378-ef29fb9b-a546-466e-ba7d-393ffa5bd589.png">


사용 개념
=======

#. App Architecture
-------------------
- App내의 각 classes들과 각 관계들을 디자인 하는 방법을 의미한다. (코드가 어떻게 구성될 것인지, 특정 시나리오에서 실행이 잘되는지, 유지보수가 용이)
- AAC(Android Architecture Component)는 MVVM패턴과 유사하다.

#. 관심사 분리
------------
- 각 Class들이 각각 구별된 부문(관심사)으로 나누는 디자인 원칙이다.
- UI Controller(Activity or Fragment), `ViewModel`, `ViewModelFactory`

- UI Controller
  - UI를 base로 하는 class이다. `Activity`, `Fragment`가 여기에 해당한다.
  - UI를 다루는 로직, 사용자 이벤트를 감지하는 로직, Andoird OS와 상호작용하는 로직만 포함한다.
  - UI Controller에는 비즈니스 로직이 포함되어 있으면 안된다. TextView를 띄우기 위해서 어떤 조건이 실행되어야 하는 로직이 포함되면 X, TextView가 출력 O
  - `GameFragment`에서는 관심사 분리 원칙에 따라, 게임 실행에 필요한 UI요소들을 화면에 그리는 역할, 사용자가 언제 버튼을 tab했는지 만을 한다.
  - tab을 한 후, game 실행에 필요한 data들은 `GameViewModel`이 담당한다.

- ViewModel
  - `ViewModel`은 관련된 UI Controller(Activity, Fragment) 화면에 보여줄 data를 저장하고 관리하는 class이다.  
  - UI Contrller에 보여줄 data를 hold, 이러한 data를 처리하는 로직(계산, data 전송 등)을 포함한다.
  - 비즈니스 로직(의사 결정 로직)을 포함한다.
 
- ViewModelFactory
  - `ViewModelFactory`는 viewModel object를 인스턴스화 한다. (생성자 파라미터가 없는 경우 / 있는 경우)

<img width="832" alt="image" src="https://user-images.githubusercontent.com/37657541/170913856-d78f3721-97ef-494b-88d5-e6fe5bbfaabd.png">


#. `ViewModel`
-------------
- `ViewModel`은 UI Controller에 필요한 data를 저장하고 관리하는 class
- 우선 여기서는 하나의 fragment에 하나의 viewModel이 연관되도록 한다.
- `viewModel`은 UI Controller와 연관이 있어야 한다.
- 연관된 UI Controller가 fragment 인 경우에는 fragment가 detach될 때, Activity인 경우에는 finish될 때, viewModel의 `onClear()`가 호출된다.
- runtime때 장치의 구성이 변경 되면(화면 회전 등) UI controller는 다시 재생성 된다. 하지만, `ViewModel` 객체는 살아있다.(ViewModelProvider를 사용하기 때문)
- viewModel 객체를 instance화 할 때에는 `ViewModelPrivider`를 사용한다. 그냥 `ViewModel`을 통해 생성하면, UI Controller와 마찬가지로 runtime 장치 구성이 변경될 때  
재생성 된다.
- `ViewModel`은 runtime때 장치 구성 변경이 되어도, 객체가 살아 있기 때문에, 장치 구성 변경이 되어도 필요한 data를 저장하기에 좋은 instance이다.  
- `Fragment`가 재생성 후, 다시 보여줄 data는 `viewModel`이 data 상태를 유지한 채로 살아있기 때문이다.
  - 화면에 보여줄 data, 이 data를 처리하는 로직을 `viewModel` class에 작성한다.
  - `viewModel`은 어떠한 경우에도, `fragment`, `Activity`, `view`의 참조를 가져서는 안된다. 이 3가지는 runtime 때 장치 구성이 변경되었을 때 모두 destroy되기 때문이다.
<img width="635" alt="image" src="https://user-images.githubusercontent.com/37657541/170914956-24c34c5a-6158-4ee5-8ffd-a1378afbecbf.png">

- `GameFragment`와 `GameViewModel`을 대입해보면, 다음과 같다.
<img width="951" alt="image" src="https://user-images.githubusercontent.com/37657541/170916304-fae0a1a2-4d5f-46d1-b8f9-55652a57fbc5.png">



#. `ViewModelProvider`
----------------------
- `ViewModelProvider`는 존재 하는 ViewModel객체를 return. 존재하지 않는 다면 새로운 ViewModel 객체를 생성.
- ViewModel객체가 생성될 때, UI Controller(Activity or Fragment)의 scope에 연관되어 생성된다.
- 생성된 ViewModel객체는, UI Controller의 scope에 맞게 지속된다. Fragment인 경우, Fragment가 detach될 때 까지 지속된다.

```kotlin
// viewModel 프로퍼티 선언
private var lateinit viewModel: GameViewModel

class GameFragment : Fragment() {
  
  override private fun onCreateView(...) {
    
    ...
    viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
    ...
  }
}
```

#. `ViewModelFactory`
----------------------
- 파라미터가 있는 생성자가 존재하는 `ViewModel`을 생성 할 때, 사용한다.
- 'ScoreViewModelFactory'를 생성하고, ViewModelProvider.Factory를 사용해, `create()`를 override한다.
- `viewModel`이 init될 때, 초기화를 진행해야하는 경우, 생성자 파라미터로 data를 넘기는 경우가 존재할 수 있다.

```kotlin
class ScoreViewModel(finalScore: Int) : ViewModel() {
   // The final score
   var score = finalScore
   init {
       Log.i("ScoreViewModel", "Final score is $finalScore")
   }
}
```

```kotlin
class ScoreViewModelFactory(finalScore: Int) : ViewModelPrivider.Factory {

  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
   if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
       return ScoreViewModel(finalScore) as T
   }
   throw IllegalArgumentException("Unknown ViewModel class")
}
```

- UI Controller인 `ScoreFragment`에서 다음과 같이 사용한다.
```kotlin
viewModelFactory = ScoreFragmentFactory(12)
viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)
```


#. `LiveData`
-------------
- `LiveData`는 lifecycle을 인식하는 observerble한 data holder class이다.
-  예를 들어, score를 `LiveData`를 사용해 wrap할 수 있다.

- `LiveData`는 관찰가능(observerable)하다.  
   이 의미는 `LiveData`가 wrap한(hold한) data가 변경 되었을 때, 관찰하고 있는 대상 'observer'가 변경 사항을 알린다.
- `LiveData`는 data를 hold한다. 어떠한 타입의 data도 wrap할 수 있다.
- `LiveData`는 생명 주기를 인식한다. LiveData에 observer를 등록 하면, observer는 `LifeCycleOwner`(일반적으로 `Activity` 혹은 `Fragment`)에 관련된다.
- `LiveData`는 생명 주기에서 active 상태인 `ACTIVE`, `RESUME`상태인 경우 또는 observer들이 'inactive -> active'의 상태 변화가 있을 때, observer들을 update한다.
- `MutableLiveData`는 변경 가능한 `LiveData`이다.

- `ScoreViewModel`에서 
```kotlin
class ScoreViewModel : ViewModel() {
  
  private val score = MutableLiveData<Int>()
  private val word = MutableLiveData<String>()
  
  init {
    // 초기화..
    score = 0
    word = ""
  }
}
```

#. viewLifecycleOwner
---------------------
- `LiveData`에 observer를 달기 위해서는 fragment에서는 `viewLifecycleOwner`를 사용해야 한다.
- Fragment의 view는 다른 fragment로 navigate하면, fragment 자체는 destroy되지 않았지만, Fragment View는 destroy된다.
- 결국에는 lifecycle이 2개가 존재한다. fragment's lifecycle 그리고 fragment view's lifecycle
- Fragment의 lifeCycle을 사용하면, 버그가 생긴다. 따라서 observer를 `onCreateView()`에서 생성하고, `viewLifecycleOwner`를 사용한다.
```kotlin
viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
  // UI 업데이트
})
```

#. `LiveData` vs `MutableLiveData`
---------------------------------
- app내에서 `viewModel` data를 변경 할 수 있다.
- UI Controller는 data를 viewModel로 부터 read해야 한다. 따라서, `viewModel`의 data field는 private이 될 수 없다.  
  이를 해결하면서, data를 `encapsulation`을 하기 위해서는 `MutableLiveData`와 `LiveData`를 사용해야 한다. 그리고 Kotlin의 backing property..
  - `MutableLiveData`에 있는 data는 변경 가능하다. `ViewModel`에서는 data를 변경 해야만 하기 때문에 `MutableLiveData`를 사용한다.
  - `LiveData`에 있는 data는 read만 가능한다. 따라서, `ViewModel` 외부에서는 data를 read 가능해야하고, write는 불가능해야하기 때문에 `LiveData`를 사용한다. 
  ```kotlin
  private val _score = MutableLiveData<Int>()
  val score: LiveData<Int>
    get() = _score
    
  init {
    _score.value = 0
  }
  ```
  
  
#. Observer Pattern
--------------------
<img width="598" alt="image" src="https://user-images.githubusercontent.com/37657541/170924977-7124962f-5735-4677-963d-1fdcc7a39983.png">
- `LiveData`가 Subject가 되고,
- Observer는 `Fragment` 와 같은 UI Controller의 method가 된다.


#. DataBinding with ViewModel, LiveData
-----------------------------
- data binding의 가장 강력한 이점은 앱내 view(layout.xml)에 direct로 data를 연결하는 것이다.
- 사용 하기 전, app의 구조는 이렇다.
<img width="935" alt="image" src="https://user-images.githubusercontent.com/37657541/170926327-abaeb8f9-eb43-4226-b9cd-2b9061dd02ee.png">

- Button View와 ViewModel이 서로 direct로 소통 하지 않고, UI Controller를 사이에 두고 소통한다.

- data binding에 viewModel을 전달하는 경우는 다음과 같다.
<img width="639" alt="image" src="https://user-images.githubusercontent.com/37657541/170926854-a69d6e9e-594d-4d31-a1c2-c1f91bc43587.png">

- 이렇게 data binding에 viewModel객체를 전달 하면, view와 viewModel간의 소통을 자동화 할 수 있다.  
또한, `LiveData`를 observe하는 UI Controller에서 observer()를 사용하지 않고도, UI data update가 가능하다.

```html
<layout ...>
   <data>
       <variable
           name="gameViewModel"
           type="com.example.android.guesstheword.screens.game.GameViewModel" />
   </data>
  
  <TextView
   android:id="@+id/word_text"
   ...
   android:text="@{gameViewModel.word}"
   ... />
```
```kotlin
viewModel = ViewModelProvider(this).get(gameViewModel::class.java)
// data binding + ViewModel
binding.viewModel = viewModel
// data binding + LiveData
binding.lifecycleOwner = viewLifecycleOwner
```

#. Listeners Binding
---------------------
- `Listeners binding`은 binding 표현식으로, `onClick()`, `onZoomIn()`, `onZoomOut()`이 발생 할 때, 실행된다. 
- Data Binding은 Listener를 생성하고, view에 listener를 설정한다.
```html
<Button
   android:id="@+id/skip_button"
   ...
   android:onClick="@{() -> gameViewModel.onSkip()}"
   ... />
```

#. Transformations.map()
------------------------
- 기존의 LiveData(source `LiveData`)에 data를 조작하는 로직을 추가해 수행하는 기능을 제공하며, 새로운 LiveData 객체를 return한다.
- Observer(UI Controller의 method)가 반환된 새로운 LiveData를 observe하고 있지 않다면, transformation연산은 수행되지 않는다.
- source LiveData와 람다 함수를 파라미터로 넘긴다. 이 때, 람다 함수 수행은 main Thread에서 수행 되므로 너무 긴작업은 적합지 않다.
- 어떤 LiveData에 대해, 특정 작업을 요구될 수가 있다. 
  - 예를 들면, 날짜를 특정 format으로 다시 변경해야하는 경우.
  - 리스트 전체를 가져와서 list의 item개수를 구하는 경우보다, transformation을 사용해서 list의 item만 가저오는 경우.
```kotlin
private val _currentTime = MutableLiveData<Long>()
val currentTime: LiveData<Long>
  get() = _currentTime
  
// 기존 LiveData로 transformations 수행.
// 새로운 LiveData객체가 return
val currentTimeString = Transformations.map(currentTime) { time ->
  // Do some transformation on the input live data
  // and return the new value
  DateUtils.formatElapsedTime(time)
}
```
