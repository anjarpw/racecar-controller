if(NOT TARGET games-controller::paddleboat)
add_library(games-controller::paddleboat SHARED IMPORTED)
set_target_properties(games-controller::paddleboat PROPERTIES
    IMPORTED_LOCATION "/home/anjar/.gradle/caches/transforms-3/5f06da7fdffe06cce987f48c709ba0ce/transformed/games-controller-2.0.0-alpha01/prefab/modules/paddleboat/libs/android.x86/libpaddleboat.so"
    INTERFACE_INCLUDE_DIRECTORIES "/home/anjar/.gradle/caches/transforms-3/5f06da7fdffe06cce987f48c709ba0ce/transformed/games-controller-2.0.0-alpha01/prefab/modules/paddleboat/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

if(NOT TARGET games-controller::paddleboat_static)
add_library(games-controller::paddleboat_static STATIC IMPORTED)
set_target_properties(games-controller::paddleboat_static PROPERTIES
    IMPORTED_LOCATION "/home/anjar/.gradle/caches/transforms-3/5f06da7fdffe06cce987f48c709ba0ce/transformed/games-controller-2.0.0-alpha01/prefab/modules/paddleboat_static/libs/android.x86/libpaddleboat_static.a"
    INTERFACE_INCLUDE_DIRECTORIES "/home/anjar/.gradle/caches/transforms-3/5f06da7fdffe06cce987f48c709ba0ce/transformed/games-controller-2.0.0-alpha01/prefab/modules/paddleboat_static/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

