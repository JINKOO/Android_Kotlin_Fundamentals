DessertClicker - Starter Code
==============================

사용 개념
=======
#. `Application` Class
- app 전역의 상태를 가지는 Class.
- 라이브러리 초기화 등, app 전역에서 한번만 초기화 하고, app 전역에서 사용하고자 할 때, `Application` class를 상속받아 생성한다.  
예를 들면, Timber 라이브러리 초기화.
- Manifest 파일에서 `<application>`안에 `android:name`속성에 생성한 `Application`Class를 지정한다.
- app 내 다른 Activity보다 먼저 초기화가 이루어 진다.

#. Activity LifeCycle
---------------------
Activity는 다음과 같은 상태를 가지며, 각 상태 변화에 따른 lifecycle 관련 call back 메소드를 가지고 있다.
<img width="420" alt="image" src="https://user-images.githubusercontent.com/37657541/169070325-5b80b0c7-106a-426a-a25d-bb3c88206991.png">

- `onCreate()` : lifecycle동안 오직 한 번만 호출된다. `setContentView(lauout)`을 통해 layout초기화, data binding초기화, View의 초기화, 클릭 리스너 초기화 작업 등을 한다.
- `onStart()` : 화면이 사용자에게 보여진다. lifecycle 동안 여러번 호출 될 수 있다. background에서 foreground로 올라올때 호출된다. `onStop()`과 쌍을 이룬다.
- `onResume()` : 화면이 사용자에게 보여지고, 현재 화면에 focus를 주고, 사용자와 상호작용을 할 수 있게 한다. Resume상태에서 focus를 잃으면, onPause()가 호출 된다.
- `onPasue()` : Resume상태에서, focus를 잃었을 때, 호출된다. 
- `onStop()` : focus를 잃고, 현재 화면에 Activity가 완전히 가려졌을 때, 호출된다. background로 이동할 때 호출된다. 이때, `Activity` instance는 아직 메모리에 존재한다.
- `onDestroy()`: 2가지 경우가 존재 한다.
  - RunTime때 장치 구성 변경 으로(화면 회전 등) 시스템에 의해 `onDestroy()`가 호출되는 경우
    - onDestroy()호출 후, 시스템이 곧 바로 새로운 Activity instance로 `onCreate()`가 호출된다.
    - `isFinishing()`값이 false
  - 사용자가 `Activity`를 완전히 닫거나, `Activity`에서 `finish()`가 호출되는 경우
    - Activity 종료
    - `isFinishing()`값은 true
- `onRestart()` : `onStop()`이 호출되어, 다시 Activity가 수행 될때 호출(예를 들면, background에 있다가 foreground로 올라올 때)

#. 시나리오
- `Activity`가 실행 될 때: `onCreate()` -> `onStart()` -> `onResume()`일 때, 이후, 다음 시나리오에서 어떻게 동작할까?
1. 현재 `Activity`에서 사용자가 home button을 tab했을 때 : `onPause()` -> `onStop()` (이후, 다시 실행하면, `onRestart()` -> `onStart()` -> `onResume()`)
2. 현재 `Activity`에서 사용자가 back button을 tab했을 때 : `onPause()` -> `onStop()` -> `onDestroy()`
3. 현재 `Activity`에서 `finish()`가 실행 될 때 : `onPause()` -> `onStop()` -> `onDestroy()`
4. 현재 `Activity`에서 사용자가 App bar의 '공유하기' 버튼 tab하여, 대화 상자가 열렸을 때 : `onPause()` 이 경우는 Activity가 완전히 가려지지 않고, focus만 잃은 상태  
foucs만 잃은 상태에서는 `onPasue()` -> `onResume()`이 된다.(`onStop()`거치지 않음)


#. Fragment LifeCycle
---------------------
<img width="426" alt="image" src="https://user-images.githubusercontent.com/37657541/169070513-93309008-6e1d-4985-86f3-e9ebb500ab3c.png">

- `onAttach()` : `Fragment`가 host `Activity`와 연결될 때 호출 된다. fragment lifecycle동안 오직 한번만 실행
- `onCreate()` : `Activity`의 `onCreate()`와 유사하다. layout보다 중요한 작업의 초기화를 할 때 사용한다. fragment lifecycle동안 오직 한번만 실행
- `onCreateView()` : layout을 inflate한다.
- `onViewCreated()` : `onCreateView()`에서 return된 view객체가 전달 된다. view의 초기값을 set, LiveData observing등의 기능을 여기에 작성한다.
- `onStart()` : `Fragment`가 화면에 보여 질 때 실행 된다. `Activity`의 `onStart()`와 같은 시점에 있다.
- `onResume()` : `Fragment`가 사용자와 상호작용을 하기 위해 호출된다. `Activity`의 `onResume()`와 같은 시점에 있다.
- `onPause()` : focus를 잃었을 때 호출 된다. `Activity`의 `onPause()`와 같은 시점에 있다.
- `onStop()` : 화면에서 완전히 보이지 않을 때 호출 된다. ``Activity`의 `onStop()`와 같은 시점에 있다.`
- `onDestroyView()` : fragment의 view가 더 이상 필요하지 않을 때, 호출 된다. 이 view와 관련된 resource들을 모두 해제 한다.
- `onDestroy()` : `fragment`가 더 이상 필요 없을 때 호출 된다.
- `onDettach()` : host `Activity`와 fragment가 연결이 해제 될 때 실행 된다.


[참고 : The Android Lifecycle cheat sheet](https://medium.com/@JoseAlcerreca/the-android-lifecycle-cheat-sheet-part-i-single-activities-e49fd3d202ab)

