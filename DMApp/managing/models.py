from django.db import models
from django.contrib.auth.models import User

# Create your models here.
GENDER_CHOICES = (
    ('male', 'Male'),
    ('female', 'Female'),
    ('other', 'Other'),
)

STATUS = (
    ('recovering', 'Recovering'),
    ('critical', 'Critical'),
    ('died', 'Died'),
)
    

class Victim(models.Model):
    name = models.CharField(max_length=50, blank=True)
    age = models.PositiveIntegerField(default=21, blank=True)
    gender = models.CharField(max_length=6, choices=GENDER_CHOICES, default='Male', blank=True)
    status = models.CharField(max_length=10, choices=STATUS)
    date = models.DateTimeField(auto_now_add=True)
    thumb = models.ImageField(default='default.jpg')
    hospital_address = models.TextField(default='Add Hospital/Relief Camp Address', blank=False)
    admitter = models.ForeignKey(User, default=1, blank=False, on_delete=models.CASCADE)
    

    def __str__(self):
        return '{ name:"' + self.name + '",age:"' + str(self.age) + '",gender:"'+self.gender + '",status:"'+self.status + '",location:"' + self.hospital_address + '",admitter:"'+ str(self.admitter) +'",image:"'+self.thumb.url + '"}'
