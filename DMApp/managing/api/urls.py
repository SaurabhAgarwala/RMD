from managing.api.views import (
    api_get_victim,
    api_update_victim,
    api_delete_victim,
    api_create_victim,
    api_get_all_victims,
    ApiVictimListView,
    VictimViewSet
)
from django.urls import path

from rest_framework.routers import DefaultRouter
from django.conf.urls import include

app_name = 'managing_api'

router = DefaultRouter()
router.register(r'victims', VictimViewSet)

urlpatterns = [
    path('get_victim/<name>', api_get_victim, name='get_victim_api'),   
    path('get_all_victims', api_get_all_victims, name='get_all_victims_api'),   
    path('update_victim/<name>', api_update_victim, name='update_victim_api'),   
    path('delete_victim/<name>', api_delete_victim, name='delete_victim_api'),   
    path('create_victim', api_create_victim, name='create_victim_api'),   
    path('list_victims', ApiVictimListView.as_view(), name='list_victims_api'),   
    path('viewsets/', include(router.urls)), 
]