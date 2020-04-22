from django.contrib import admin
from django.conf.urls import url, include
from django.contrib.staticfiles.urls import staticfiles_urlpatterns
from django.conf.urls.static import static
from django.conf import settings

urlpatterns = [
    url(r'^admin/', admin.site.urls),
    url(r'^managing/', include('managing.urls')),
    url(r'^accounts/', include('accounts.urls')),
    url(r'^donate/', include('donate.urls')),
    url(r'^api/managing/', include('managing.api.urls')),
    url(r'^api/accounts/', include('accounts.api.urls')),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework'))
]

urlpatterns += staticfiles_urlpatterns()
urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)