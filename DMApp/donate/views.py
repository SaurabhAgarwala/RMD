from django.shortcuts import render, redirect
from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader


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
    template = loader.get_template('donate/challan.html')
    context = {}
    return HttpResponse(template.render(context, request))

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

