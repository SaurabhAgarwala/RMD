# Generated by Django 2.0.5 on 2018-10-24 14:33

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('managing', '0015_victim_admitter'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='victim',
            name='victim_photo',
        ),
        migrations.AddField(
            model_name='victim',
            name='victim_Photo',
            field=models.ImageField(blank=True, upload_to=''),
        ),
    ]