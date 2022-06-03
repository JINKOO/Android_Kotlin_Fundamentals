실행화면
========
- RecyclerView 적용 전 
<img width="949" alt="image" src="https://user-images.githubusercontent.com/37657541/171532994-e36d1c02-4274-440d-b1c3-476b0f56add4.png">

- `SleepTrackerFragment`에 RecyclerView 적용
<img width="319" alt="image" src="https://user-images.githubusercontent.com/37657541/171533209-418cb7e7-fe0d-4904-b0f2-29ac7a5687a3.png">

Architecture
===============
- 여기서는 UI Controller, ViewModel, Room을 사용해 간단화 시킨 다음과 같은 아키텍처를 사용한다.(아직 Repository pattern은 적용하지 않음)
<img width="322" alt="image" src="https://user-images.githubusercontent.com/37657541/171533778-580e248e-5b50-4c25-85cf-a7a5def4c581.png">


사용개념
=======

#.Room
--------

#.Coroutine
------------
루틴 루틴 코루틴, 드디어 코루틴이구나 솔직히 처음 다뤄봐서 아직도 이해가 잘 안가지만, 그래도 정리는 해봄.
앞으로 3번은 더 볼거임!!!

#.`RecyclerView`
-----------------

#. `LiveData`를 사용한 Button State Control 
-----------------
- click handler안에서 `LiveData`를 사용해, fragment에서 다른 destination(fragment)으로 navigate하도록 설정할 수 있다.
- 여기서는 `SleepTrackerFragment`에서, 사용자가 start버튼을 tab하면 safe-args(기록할 sleepNight의 id)를 가지고 `SleepTrackerFragment`로 이동한다.  
  다음 그림과 같다.
<img width="680" alt="image" src="https://user-images.githubusercontent.com/37657541/171789287-66bd25a9-426f-48d6-9577-5e9dd2ef6886.png">

- 원리는 `LiveData`를 `Observe`하는 UI Controller가(여기서는 `SleepTrackerFragment`) `LiveData`의 값이 변경되면, 특정 destination(여기서는 `SleepQualityFragment`)으로 이동하고, 이동 완료함을 `ViewModel`(여기서는 `SleepTrackerFragment`)에게 알린다.

- SleepTrackerViewModel에서...
```kotlin
class SleepTrackerViewModel() : ViewModel() {

  // SleepQuality로 이동하는 navigate trigger
  private val _onNavigateToSleepQuality = MutableLiveData<SleepNight?>()
  
  val onNavigateToSleepQuality: LiveData<SleepNight?>
    get() = _onNavigateToSleepQuality
  
  ...
  
  // 이동완료함을 설정한다.
  fun onNavigateDone() {
    _onNavigateToSleepQuality.value = null
  }
  ...
}
```

- SleepTrakcerFragment에서...
```kotlin
override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
  
  ...
  // viewModel의 navigate trigger mutableLiveData observe
  viewModel.onNavigateToSleepQuality.observe(viewLifecycleOwner, Observer { sleepNight ->
    sleepNight?.let {
      // 1. 상태가 변화되면 sleepQualityFragment로 이동
      moveToSleepQuality()
      
      // 2. 이동완료함을 ViewModel에 알린다.
      viewModel.doneNavigate()
    }
  })
  ....
}
```

- `SleepQualityFragment`에서 사용자가 SleepQuality를 선택하면, `SleepTrackerFragment`로 이동해야한다.  
  여기서도, 위와 같은 방식으로, navigate trigger를 나타내는 `LiveData`를 사용한다.

- SleepQualityViewModel에서...
```kotlin
class SleepQualityViewModel(
  private val database: SleepDatabaseDao,
  private val sleepNigthKey: Long = 0L
) : ViewModel() {
  
  // sleepQualityFragment로 navigate하는 trigger LiveData
  private val _onNavigateToSleepTracker = MutableLiveData<Boolean?>()
  val onNavigateToSleepTracker: LiveData<Boolean>
    get() = _onNavigateToSleepTracker
  
  // navigate가 완료됨을 liveData에 알린다.
  fun onNavigateDone() {
    _onNavigateToSleepTracker.value = false
  }
  ...
}
```

- SleepQualityFragment에서...
```kotlin
override fun onCreateView(inflater: LayoutInflater, 
     container: ViewGroup?,
     savedInstanceState: Bundle?): View? {
     
     ...
     viewModel.onNavigateToSleepTracker.observe(viewLifecycleOwner, Observer { toMove ->
        if (toMove) {
          // 1. SleepTrackerFragment로 이동.
          moveToSleepTrackerFragment()
          
          // 2. navigate 완료함을 viewModel에 알린다.
          viewModel.onNavigateDone()
        }
     })
     ...
}
```


