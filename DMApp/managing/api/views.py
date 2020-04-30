# Django REST Framework Course Link https://www.youtube.com/playlist?list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV

from django.http import JsonResponse, HttpResponse
from django.contrib.auth.models import User
from managing.models import Victim


from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.authentication import TokenAuthentication
from rest_framework.pagination import PageNumberPagination
from rest_framework.generics import ListAPIView
from managing.api.serializers import VictimSerializer
from rest_framework.filters import SearchFilter, OrderingFilter

from rest_framework import viewsets

@api_view(['GET', ])
@permission_classes([IsAuthenticated, ])  # Actually not requried, as already set as default in settings.py, hence not used in below methods
def api_get_victim(request, name):
    
    try:
        victim = Victim.objects.get(name=name)
    except Victim.DoesNotExist:
         return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = VictimSerializer(victim)
        return JsonResponse(serializer.data)

@api_view(['GET', ])
def api_get_all_victims(request):

    try:
        victims = Victim.objects.all()
    except Victim.DoesNotExist:
        return Response(status=404)

    if request.method == 'GET':
        serializer = VictimSerializer(victims, many=True)
        return Response(data=serializer.data, status=200)

@api_view(['PUT', ])
def api_update_victim(request, name):
    
    try:
        victim = Victim.objects.get(name=name)
    except Victim.DoesNotExist:
         return Response(status=status.HTTP_404_NOT_FOUND)

    user = request.user
    if victim.admitter != user:
        return Response({
            'response': "You aren't the original admitter, so you can't edit"
        })


    if request.method == 'PUT':
        serializer = VictimSerializer(victim, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            data = serializer.data
            data['response'] = "Update Successful"
            return JsonResponse(data=data, status=status.HTTP_202_ACCEPTED)
        return JsonResponse(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

@api_view(['DELETE', ])
def api_delete_victim(request, name):

    try:
        victim = Victim.objects.get(name=name)
    except Victim.DoesNotExist:
        return Response(status=404)

    user = request.user
    if victim.admitter != user:
        return Response({
            'response': "You aren't the original admitter, so you can't edit"
        })

    if request.method == 'DELETE':
        operation = victim.delete()
        data = {}
        if operation:
            data['success'] = "Delete Successful"
        else:
            data['failure'] = "Delete Failed"
        return JsonResponse(data=data)

@api_view(['POST', ])
def api_create_victim(request):

    account = request.user
    victim = Victim(admitter=account)

    if request.method == 'POST':

        serializer = VictimSerializer(victim, data=request.data)
        if serializer.is_valid():
            serializer.save()
            data = serializer.data
            data['response'] = "Created Successfully"
            return Response(data=data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


# For pagination demonstration
class ApiVictimListView(ListAPIView):
    queryset = Victim.objects.all()
    serializer_class = VictimSerializer
    # authentication_classes = (TokenAuthentication, )    # These 3 lines can be commented as already defined as default in settings.py
    # permission_classes = (IsAuthenticated, )
    # pagination_class = PageNumberPagination
    filter_backends = [SearchFilter, OrderingFilter]
    search_fields = ['name', 'status', 'hospital_address', 'admitter__username']


# Viewsets and Routers

# from rest_framework.decorators import action

class VictimViewSet(viewsets.ModelViewSet):
    """
    This viewset automatically provides `list`, `create`, `retrieve`,
    `update` and `destroy` actions.

    Additionally we also provide an extra `highlight` action.
    """
    queryset = Victim.objects.all()
    serializer_class = VictimSerializer
    # permission_classes = [permissions.IsAuthenticatedOrReadOnly,
                        #   IsOwnerOrReadOnly]

    # @action(detail=True, renderer_classes=[renderers.StaticHTMLRenderer])
    # def highlight(self, request, *args, **kwargs):
    #     snippet = self.get_object()
    #     return Response(snippet.highlighted)

    def perform_create(self, serializer):
        serializer.save(admitter=self.request.user)