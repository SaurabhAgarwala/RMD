from django.urls import path
from accounts.api.views import (
    registration_view,
    user_properties_view,
    update_user_view,
    ObtainAuthToken,
    GetAllUsers,
    DeleteUser,
    ChangePasswordView,
    UserViewSet
)
# from rest_framework.authtoken.views import obtain_auth_token   # Previously used function for getting the auth token

from rest_framework.routers import DefaultRouter
from django.conf.urls import include

router = DefaultRouter()
router.register(r'users', UserViewSet)

app_name = 'accounts_api'

urlpatterns = [
    path('register', registration_view, name='register'),
    # path('login', obtain_auth_token, name='login'),
    path('login', ObtainAuthToken.as_view(), name='login'),
    path('details', user_properties_view, name='detail'),
    path('update', update_user_view, name='update'),
    path('all_users', GetAllUsers.as_view(), name='all_users'),
    path('delete_user', DeleteUser.as_view(), name='delete_user'), 
    path('change_password', ChangePasswordView.as_view(), name='change_password'),
    path('viewsets/',include(router.urls)),
]