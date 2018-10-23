from django.db import models
from django.contrib.auth.models import User

# Create your models here.
GENDER_CHOICES = (
    ('Male'),
    ('Female'),
    ('Other'),
)

STATUS = (
    ('Recovering'),
    ('Critical'),
    ('Died'),
)

class Victim(models.Model):
    name = models.CharField(max_length=50, blank=True)
    age = models.IntegerField(default=0, blank=True)
    gender = models.CharField(max_length=6, choices=GENDER_CHOICES, default='Male', blank=True)
    status = models.CharField(max_length=10, choices=STATUS, default='Recovering')
    date = models.DateTimeField(auto_now_add=True)
    thumb = models.ImageField(default='default.png', blank=True)
    admitter = models.ForeignKey(User, default=None, on_delete=models.CASCADE)

    def __str__(self):
        return self.name
