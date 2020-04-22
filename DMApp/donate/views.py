from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from . import forms

from django.views.generic import View
from django.utils import timezone
from .render import Render
from datetime import datetime
import re

text = ''
# Create your views here.

def donate(request):
    template = loader.get_template('donate/donate.html')
    context = {}
    return HttpResponse(template.render(context, request))

def home(request):
    template = loader.get_template('donate/home.html')
    context = {}
    return HttpResponse(template.render(context, request))

def challan(request):
    if request.method == 'POST':
        nm = request.POST.get('name')
        amt = request.POST.get('amount')
        dt = request.POST.get('date')
        
        context = {
            'name':nm,
            'amount':amt,
            'date':dt
        }
        return Render.render('donate/gen.html',{'context':context})
    else:
        form = forms.DonorForm
        return render(request, 'donate/challan.html', {'form':form})

def help(request):
    template = loader.get_template('donate/help.html')
    context = {}
    return HttpResponse(template.render(context, request))

def man(request):
    template = loader.get_template('donate/man.html')
    context = {}
    return HttpResponse(template.render(context, request))

def about(request):
    template = loader.get_template('donate/about.html')
    context = {}
    return HttpResponse(template.render(context, request))
