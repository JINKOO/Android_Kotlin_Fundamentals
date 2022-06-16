::실행화면
==========

::사용 아키텍처
=============
- AAC with `LiveData`, `Data Binding`, `ViewModel`
- Repository Pattern
- Navigation
- Room
- Retrofit2

::사용 개념
==========
#. `Repository Pattern`
---------------------------
- app내에서 data source와 나머지 app의 부분과 분리하는 pattern이다.
- offline cache를 실현하기 위해서 사용한다(보통 `Room`을 사용한다).  
  지금까지 진행했던 프로젝트에서는 `Retrofit`을 사용해서 Internet연결을 통해서 remote에서 data를 가져와,
  이 data를 곧바로 `UI Controller`(예를 들면, `Fragment`의 `Recyclerview`)에 바로 출력했다.
  대신에 device의 local storage를 사용해, 좋지 않은 Internet환경 또는 Internet연결이 없어도, 최신의 data를 사용할 수 있게 한다. 
- 이 기법은 App내 data 접근에 있어 Clean API를 제공한다.

  <img width="866" alt="image" src="https://user-images.githubusercontent.com/37657541/173493349-f8caf6c9-a3ec-4f1b-a501-10c32e301b27.png">

-`Repository`가 data sources(Room과 같은 persistenct Model, WebService, Cache)와, `UI Controller`, `ViewModel`사이에서 중재 역할을 한다.
- 위 다이어 그램은 `LiveData`를 사용하는 `Activity`와 같은 app component들이 Repository를 통해, data sources와 상호작용하는 것을 보여준다.

#. `Repository Pattern`이점
--------------------------
- App내에서 data에 접근하는데, clean API를 제공한다.
- data operation을 다루고, 다수의 backend를 사용할 수 있도록 해준다.
- 상용 app에서는 network로 부터 data를 fetch할 지, cache가 적용된 local database에서 data를 fetch할지 결정하는 로직을 구현한다.
  그렇기 때문에, test 및 모듈화가 용이하다. 

#. `Seperation of Concerns`
---------------------------
- 관심사 분리(network object, database object, domain object의 분리)
- `DevByteVideo`프로젝트에서 패키지 구조가 다음과 같이 되어 있다.

  <img width="448" alt="image" src="https://user-images.githubusercontent.com/37657541/173485119-9d5ac1a3-968a-4f3d-9a41-3ea40306e8d5.png">

- 해당 프로젝트는 feature로 package를 구성하지 않고, component별로 구성되어 있다.
- 주목해야하는 점은, `domain` package이다.  
  domain패키지 내, data class는 실제 app내에서 관리해, UI data에 사용하는 Model Class이다.   
  여기서는 `DevByteVideo`이고, `Domain Object`라고 불린다.
- 즉, 다시 말해, Network 또는, Room 에서 가져온 Response data(network object, room object)들을 바로 화면에 보여주는 것이아니라, 각각의 Response에 대한 data class를 정의하고,  
  Domain object로 변환(map)하여, app내에서 UI data로 사용할 수 있도록 한다.
- 해당 프로젝트에서는 Network와 Room DataBase에서의 data를 `DataTransferObject`파일을 따로 만들어, 관리하고, `asDomainModel()`를 통해  
  Domain object로 변환한다. DTO object들은 network로 부터 받은 Response에 대한 파싱 작업을 해야하므로, domain object와는 다르다. 
- 이렇게 하는 이유는, network의 response 또는 database의 scheme이 변경되는 경우, 수정 작업을 최소화 하기 위해서이다.


#. Android에서의 'Caching'
------------------------
- Network로 부터 data를 fetch한 후, Android 에서 App은 해당 data를 device의 storage에 `cache`할 수 있다.
- networking cache를 실현한느데 있어 4가지 방법이 존재하는데, `Room`을 사용하는 것이 권장된다.
  1. Retrofit
  2. SharedPreference
  3. app내 internal strage
  4. `Room`


#. `Room`을 사용한 offline cache(network data cache)
--------------------------------------------------
- app이 실행 될 때마다, data를 network로 부터 fetch하는 것은 좋지 않다. 대신에, database로 부터 fetch에서 UI에 보여주도록 한다.  
  이렇게 하면, app의 loading 속도를 줄일 수 있다.
- app이 network로 부터 data를 fetch하고, 바로 UI controller에 전달해 화면에 보여주는 대신에, 해당 data들을 database에 저장한다.
- 새로운 network result가 update되면, local database또한, update한다. 이 기법을 통해, local database의 data들도 항상 최신의 data를 유지하는 것을 보장한다.
- 또한, device가 offline인 경우에도, local에 cache된 data를 가져올 수 있다.

- 우선, 'database/DatabaseEntities.kt'에 database object를 추가한다. (추후, 위에서 말한 관심사 분리 원칙에 따라, domain object로 변환하는 함수를 만들어야 한다)
  ```kotlin
  @Entity(name="database_video")
  data class DatabaseVideo(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String
  )
  
  // database object를 domain object로 변환
  // 여기서는 conversion이 쉽지만, 실제 상용 app에서는 훨씬 복잡 할 수 있다.
  fun List<DatabaseVideo>.asDomainModel(): List<DevByteVideo> {
    return map {
       DevByteVideo(
               url = it.url,
               title = it.title,
               description = it.description,
               updated = it.updated,
               thumbnail = it.thumbnail
       )
    }
  }
  ```
- Network로 부터 받아온 response(network object)역시, database object로 변환해야 한다. 
- `network/dataTransferObject.kt`에서 다음 확장 함수를 추가한다.
  
  ```kotlin
  // Network로 부터 받아온 response data. network object
  @JsonClass(generateAdatper = true)
  data class NetworkVideoContainer(
        val videos: List<NetworkVideo>
  )
  
  @JsonClass(generateAdapter = true)
  data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?
  )
  
  // network object를 database object로 변환한다.
  fun NetworkVideoContainer.asDatabaseModel(): List<DatabaseVideo> {
    return videos.map {
      DatabaseVideo(
        title = it.title,
        description = it.description,
        url = it.url,
        updated = it.updated,
        thumbnail = it.thumbnail
      )
    }
  }
  ```
- `RoomDao`를 생성하고, 2가지 helper method를 생성한다.
  1. database에 network로 부터 fetch한 data를 insert.
    - fun insertAll()
  2. database에서 data를 get.
    - fun getVideos()
  
  ```kotlin
  @Dao
  interface VideoDao {
    // network로 부터 fetch해 database에 저장된 data를 `LiveData`를 통해 return한다. 
    // 그래야, database의 data가 변경될 때, UI Controller에서 변경된 data를 refresh할 수 있기 때문.
    @Query("SELECT * FROM database_video")
    fun getVideos(): LiveData<List<DatabaseVideo>>
    
    // network로 부터 fetch한 data를 insert
    // 이미, data가 존재한다면, `OnConflictStrategy.REPLACE`을 통해, 덮어쓴다. 그래야 최신 data를 유지. 
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(videos: List<DatabaseVideo>)
  }
  ```

- `Repository`추가
  ```kotlin
  // network로부터 fetch한 response를 database에 저장하고, 해당 data를 fetch하는 로직 포함. 
  class VideoRepository(private val database: VideoDatabase) {
  
    // database로 부터 cahce된 최신화 된 data를 가져온다.
    // Transformation을 사용한 이유는, database object를 domain object로 변경해, UI Controller에서 사용하기 위해.
    // LiveData가 return 되므로, database의 data가 변경될 때 마다, UI Controller에서는 refresh된 data들을 사용 할 수 있다.
    val videos = Transformations.map(database.videoDao.getVideos()) {
      // 실제 app내에서 UI에 보여줄 data로 변환.
      it.asDomainModel()
    }
  
    // offline cache된 data를 refresh 하는 API가 된다.
    suspen fun refreshVideos() {
      withContext(Dispatchers.io) {
        // 1. network로 부터 data fetch
        val videos = VideoApi.retrofitService.getVideos()
        
        // 2. database에 insert(database object로 변환 후)
        database.videoDao.insertAll(videos.asDatabaseModel())
      }
    }
  }
  ```
  
#. `withContext(Dispatchers.io)`
--------------------------------
- 추후 설명 추가 예정! 아직 이해가 가질 않아서..

#. repository integrate with refresh strategy
----------------------------------------------
- `database refresh`란, network에 있는 data와 sync를 맞추기 위해, update 또는 refresh하는 프로세스이다.
- 여기서 사용하는 가장 간단한 refresh strategy는 repository에 data를 요청한 모듈은, local data를 최신화 하는데 책임을 가진다. 라는 전략을 사용한다.
- 여기서는 `ViewModel`을 사용한다.
  ```kotlin
  class DevByteViewModel(
    application: Application
  ) : AndroidViewModel(application) {
    
    // Repository로 부터, data를 fetch한다. 요청하는 놈은 이 viewModel이다.
    private val videoRepository = VideoRepository(getDatabase(application))
    
    val videos = videoRepository.videos
    
    init {
      // data를 요청하는 놈이 이 viewModel이므로, data를 최신화 하는데 책임을 가진다. 
      refreshDataFromRepository()
    }
    
    fun refreshDataFromRepository() {
      try {
        // data를 요청하고, 최신화 시킨다.
        // refreshVideo안에 network로 부터 data를 최신회 시키는 부분이 있다. 
        // network로 부터 바로 fetch하는 것이 아니라, database로 부터 fetch가 이루어 진다.
        videoRepository.refreshVideos()
        ...
      } catch (e: Exception) {
        ...
      }
    
    }
  
  }
  ```
  
