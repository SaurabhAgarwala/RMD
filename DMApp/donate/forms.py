from django import forms
from . import models

class DonorForm(forms.ModelForm):
    class Meta:
        model = models.Donor
        fields = ['name', 'amount']

