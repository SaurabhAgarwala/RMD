# Generated by Django 2.0.5 on 2018-10-24 13:49

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('managing', '0002_auto_20181024_1915'),
    ]

    operations = [
        migrations.AlterField(
            model_name='victim',
            name='admitter',
            field=models.ForeignKey(default=None, on_delete=django.db.models.deletion.CASCADE, to=settings.AUTH_USER_MODEL),
        ),
    ]
