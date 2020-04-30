# Django REST Framework Course Link https://www.youtube.com/playlist?list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV

# The views defined here are the most basic ones, and doesn't handle all odd scenarios. For a bit more 
# detail refer the gists present here https://www.youtube.com/watch?v=JFJXckWoy00&list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV&index=13

from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes, authentication_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication

from django.http import JsonResponse, HttpResponse

from accounts.api.serializers import RegistrationSerializer, UserPropertiesSerializer, ChangePasswordSerializer
from rest_framework.authtoken.models import Token

from rest_framework.views import APIView
from rest_framework.generics import ListAPIView, UpdateAPIView, DestroyAPIView
from django.contrib.auth import authenticate
from django.contrib.auth.models import User

from rest_framework import viewsets

@api_view(['POST', ])
@permission_classes([])
def registration_view(request):
    
    if request.method == 'POST':
        serializer = RegistrationSerializer(data=request.data)
        data = {}
        if serializer.is_valid():
            user = serializer.save()
            data['response'] = "New user successfully registered"
            data['email'] = user.email
            data['username'] = user.username
            token = Token.objects.get(user=user).key
            data['token'] = token
            return Response(data, status=status.HTTP_201_CREATED)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        


@api_view(['GET', ])
@permission_classes([IsAuthenticated, ])
def user_properties_view(request):
    try:
        user = request.user
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = UserPropertiesSerializer(user)
        return Response(serializer.data)


@api_view(['PUT', ])
@permission_classes([IsAuthenticated, ])
def update_user_view(request):
    try:
        user = request.user
    except User.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'PUT':
        serializer = UserPropertiesSerializer(user, data=request.data, partial=True)
        data = {}
        if serializer.is_valid():
            serializer.save(email=serializer.validated_data['email'].lower())
            data['response'] = "User account updated successfully"
            data['email'] = user.email
            data['username'] = user.username
            return Response(data=data, status=status.HTTP_202_ACCEPTED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# Updating the Login view (previously imported function) i.e. customizing it to
# display error meesages as well
# instead of just returning the token by using obtain_auth_token function
class ObtainAuthToken(APIView):

    authentication_classes = []
    permission_classes = []

    def post(self, request):
        
        context = {}

        username = request.POST.get('username')
        password = request.POST.get('password')
        account = authenticate(username=username, password=password)
        if account:
            try:
                token = Token.objects.get(user=account)
            except Token.DoesNotExist:
                token = Token.objects.create(user=account)
            context['response'] = 'Successfully Authenticated'
            context['pk'] = account.pk
            context['username'] = username
            context['token'] = token.key
        else:
            context['response'] = 'Error'
            context['error_message'] = 'Invalid credentials entered'


        return Response(data=context)


class GetAllUsers(ListAPIView):
    serializer_class = UserPropertiesSerializer
    queryset = User.objects.all()
    # Default Authentication Applied

class DeleteUser(DestroyAPIView):
    model = User
    queryset = User.objects.all()
    # Default Authentication Applied

    def get_object(self, queryset=None):
        obj = self.request.user
        return obj

    def delete(self, request, *args, **kwargs):
        self.object = self.get_object()
        success = self.object.delete()
        if success:
            return Response({"response":"Successfully deleted user"}, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ChangePasswordView(UpdateAPIView):

    serializer_class = ChangePasswordSerializer
    model = User
    permission_classes = (IsAuthenticated,)
    authentication_classes = (TokenAuthentication,)

    def get_object(self, queryset=None):
        obj = self.request.user
        return obj

    def update(self, request, *args, **kwargs):
        self.object = self.get_object()
        serializer = self.get_serializer(data=request.data)

        if serializer.is_valid():
            # Check old password
            if not self.object.check_password(serializer.data.get("old_password")):
                return Response({"old_password": "Wrong old password entered."}, status=status.HTTP_400_BAD_REQUEST)

            # confirm the new passwords match
            new_password = serializer.data.get("new_password")
            confirm_new_password = serializer.data.get("confirm_new_password")
            if new_password != confirm_new_password:
                return Response({"new_password": "New passwords must match"}, status=status.HTTP_400_BAD_REQUEST)

            # set_password also hashes the password that the user will get
            self.object.set_password(new_password)
            self.object.save()
            return Response({"response":"Successfully changed password"}, status=status.HTTP_200_OK)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# Viewsets and Routers

class UserViewSet(viewsets.ReadOnlyModelViewSet):
    """
    This viewset automatically provides `list` and `detail` actions.
    """
    queryset = User.objects.all()
    serializer_class = UserPropertiesSerializer