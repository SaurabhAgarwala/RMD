from django import forms
from . import models

class VictimForm(forms.ModelForm):
    class Meta:
        model = models.Victim
        fields = ['name', 'age', 'gender', 'status', 'hospital_address', 'thumb']

