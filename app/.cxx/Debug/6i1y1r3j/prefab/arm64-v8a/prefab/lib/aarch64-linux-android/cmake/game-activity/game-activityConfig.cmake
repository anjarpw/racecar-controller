if(NOT TARGET game-activity::game-activity)
add_library(game-activity::game-activity INTERFACE IMPORTED)
set_target_properties(game-activity::game-activity PROPERTIES
    INTERFACE_INCLUDE_DIRECTORIES "/home/anjar/.gradle/caches/transforms-3/c9d8e9b65fa583cf14531ec728d824f8/transformed/games-activity-1.0.0/prefab/modules/game-activity/include"
    INTERFACE_LINK_LIBRARIES ""
)
endif()

