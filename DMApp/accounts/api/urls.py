from django.urls import path
from accounts.api.views import (
    registration_view,
    user_properties_view,
    update_user_view,
    ObtainAuthToken
)
from rest_framework.authtoken.views import obtain_auth_token

app_name = 'accounts_api'

urlpatterns = [
    path('register', registration_view, name='register'),
    # path('login', obtain_auth_token, name='login'),
    path('login', ObtainAuthToken.as_view(), name='login'),
    path('details', user_properties_view, name='detail'),
    path('update', update_user_view, name='update'),
]