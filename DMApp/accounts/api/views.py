# Django REST Framework Course Link https://www.youtube.com/playlist?list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV

# The views defined here are the most basic ones, and doesn't handle all odd scenarios. For a bit more 
# detail refer the gists present here https://www.youtube.com/watch?v=JFJXckWoy00&list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV&index=13

from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated

from django.http import JsonResponse, HttpResponse

from accounts.api.serializers import RegistrationSerializer, UserPropertiesSerializer
from rest_framework.authtoken.models import Token

from rest_framework.views import APIView
from django.contrib.auth import authenticate

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
        else:
            data = serializer.errors
        return Response(data)


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
        return Response(status=404)

    if request.method == 'PUT':
        serializer = UserPropertiesSerializer(user, data=request.data)
        data = {}
        if serializer.is_valid():
            serializer.save()
            data['response'] = "User account updated successfully"
            return Response(data=data)
        return Response(serializer.errors, status=400)


# Updating the Login view i.e. customizing it to display error meesages as well
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
