from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from . import forms


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
        #return render(request, 'donate/challan.html', {'context':context})
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
