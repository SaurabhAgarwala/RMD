from django.db import models
from django.contrib.auth.models import User

class Donor(models.Model):
	name = models.CharField(max_length=50)
	amount = models.PositiveIntegerField(default=100)
	date = models.DateTimeField(auto_now_add=True)
