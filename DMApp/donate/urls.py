from django.conf.urls import url
from . import views

app_name = 'donate'

urlpatterns = [
   url(r'^home/', views.donate, name="donate"),
   url(r'^help/', views.help, name="help"),
   url(r'^challan/', views.challan, name="challan"),
   url(r'^$', views.home, name="home"),
   url(r'^manual', views.man, name="man"),
   url(r'^about', views.about, name="about"),
]   