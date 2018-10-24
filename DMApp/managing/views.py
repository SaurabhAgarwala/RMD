from django.shortcuts import render, redirect
from .models import Victim
from django.http import HttpResponse
from django.contrib.auth.decorators import login_required
from . import forms
import json
from django.http import JsonResponse
from django.forms.models import model_to_dict

# Create your views here.

def victim_list(request):
    victims = Victim.objects.all().order_by('date')
    return render(request, 'managing/victim_list.html', {'victims':victims})

def app_victimlist(request):
    victims = Victim.objects.all().order_by('date')
    return json.dumps(victims)

@login_required(login_url="/accounts/login/")
def victim_new(request):
    if request.method == 'POST':
        form = forms.VictimForm(request.POST, request.FILES)
        if form.is_valid():
            s_instance = form.save(commit=False)
            s_instance.admitter = request.user
            s_instance.save()
            return redirect('managing:list')
    else:
        form = forms.VictimForm
    return render(request, 'managing/victim_enter.html', {'form':form})