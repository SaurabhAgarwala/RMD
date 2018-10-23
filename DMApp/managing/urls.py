from django.conf.urls import url
from . import views 

app_name = 'managing'

urlpatterns = [
    url(r'^$', views.victim_list, name="list"),
    url(r'^enter/$', views.victim_new, name="enter")
]
