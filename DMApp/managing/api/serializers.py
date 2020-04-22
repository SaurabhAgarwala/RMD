# Django REST Framework Course Link https://www.youtube.com/playlist?list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV

from rest_framework import serializers
from managing.models import Victim

class VictimSerializer(serializers.ModelSerializer):
    
    admitter = serializers.SerializerMethodField('get_admitter_of_victim')
    
    class Meta:
        model = Victim
        fields = ['name', 'age', 'gender','status', 'date', 'thumb', 'hospital_address', 'admitter']

    def get_admitter_of_victim(self, victim):
        admitter = victim.admitter.username
        return admitter