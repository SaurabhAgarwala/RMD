# Django REST Framework Course Link https://www.youtube.com/playlist?list=PLgCYzUzKIBE9Pi8wtx8g55fExDAPXBsbV

from rest_framework import serializers
from django.contrib.auth.models import User

class RegistrationSerializer(serializers.ModelSerializer):

    password2 = serializers.CharField(
        style = {'input_type': 'password'},
        write_only = True
    )

    class Meta:
        model = User
        fields = ['email', 'username', 'password', 'password2']
        extra_kwargs = {
            'password': {
                'write_only': True
            }
        }

    def save(self):
        user = None
        if 'email' in self.validated_data:
            try:
                user = User.objects.get(email=self.validated_data['email'].lower())
            except User.DoesNotExist:
                pass
            except User.MultipleObjectsReturned:
                user = True
            if user:
                raise serializers.ValidationError({
                    'response': 'User with entered email already exists.',
                    'email_entered': self.validated_data['email'].lower()
                })
        password1 = self.validated_data['password'],
        password1 = password1[0] # This gets converted to tuple in above line, no idea how. That's why have to use this
        password2 = self.validated_data['password2']
        if password1 != password2:
            raise serializers.ValidationError({
                'password': 'Passwords do not match.',
                'password1': password1,
                'password2': password2   
        })
        user = User(
            email = self.validated_data['email'].lower() if 'email' in self.validated_data else '',
            username = self.validated_data['username'],
        )
        user.set_password(password1)
        user.save()
        return user


class UserPropertiesSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = ['email', 'username']

    def validate(self, data):
        user = None
        if 'email' in data:
            try:
                user = User.objects.get(email=data['email'].lower())
            except User.DoesNotExist:
                pass
            except User.MultipleObjectsReturned:
                user = True
            if user:
                raise serializers.ValidationError({
                    'response': 'User with entered email already exists.',
                    'email_entered': data['email'].lower()
            })
        if 'username' in data:
            try:
                user = User.objects.get(username=data['username'])
            except User.DoesNotExist:
                pass
            if user:
                raise serializers.ValidationError({
                    'response': 'User with entered username already exists.',
                    'username_entered': user.username    
                })
        return data


class ChangePasswordSerializer(serializers.Serializer):

	old_password = serializers.CharField(required=True)
	new_password = serializers.CharField(required=True)
	confirm_new_password = serializers.CharField(required=True)