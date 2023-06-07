import re
import time

import requests
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.common.by import By

from AudioUrlDecoder import decode_audio_url
from Utils import set_cookies_from_list

RE_M3U8_TO_MP3 = re.compile(r'/[0-9a-f]+(/audios)?/([0-9a-f]+)/index.m3u8')

RPS_DELAY_RELOAD_AUDIO = 1.5

TRACKS_PER_USER_PAGE = 2000
TRACKS_PER_ALBUM_PAGE = 2000


class VkAudio(object):
    """ Модуль для получения аудиозаписей без использования официального API.

        :param vk: Объект :class:`VkApi`
        """
    __slots__ = ('_vk', 'user_id', 'convert_m3u8_links')

    DEFAULT_COOKIES = [
        {  # если не установлено, то первый запрос ломается
            'version': 0,
            'name': 'remixaudio_show_alert_today',
            'value': '0',
            'port': None,
            'port_specified': False,
            'domain': '.vk.com',
            'domain_specified': True,
            'domain_initial_dot': True,
            'path': '/',
            'path_specified': True,
            'secure': True,
            'expires': None,
            'discard': False,
            'comment': None,
            'comment_url': None,
            'rfc2109': False,
            'rest': {}
        }, {  # для аудио из постов
            'version': 0,
            'name': 'remixmdevice',
            'value': '1920/1080/2/!!-!!!!',
            'port': None,
            'port_specified': False,
            'domain': '.vk.com',
            'domain_specified': True,
            'domain_initial_dot': True,
            'path': '/',
            'path_specified': True,
            'secure': True,
            'expires': None,
            'discard': False,
            'comment': None,
            'comment_url': None,
            'rfc2109': False,
            'rest': {}
        }
    ]

    def __init__(self, vk, convert_m3u8_links=True):
        self.user_id = vk.method('users.get')[0]['id']
        self._vk = vk
        self.convert_m3u8_links = convert_m3u8_links

        set_cookies_from_list(self._vk.http.cookies, self.DEFAULT_COOKIES)

        self._vk.http.get('https://m.vk.com/')  # load cookies

    def get(self, owner_id=None, album_id=None, access_hash=None):
        """ Получить список аудиозаписей пользователя

        :param owner_id: ID владельца (отрицательные значения для групп)
        :param album_id: ID альбома
        :param access_hash: ACCESS_HASH альбома
        """

        return list(self.get_iter(owner_id, album_id, access_hash))

    def get_iter(self, owner_id=None, album_id=None, access_hash=None):
        """ Получить список аудиозаписей пользователя (по частям)

        :param owner_id: ID владельца (отрицательные значения для групп)
        :param album_id: ID альбома
        :param access_hash: ACCESS_HASH альбома
        """

        if owner_id is None:
            owner_id = self.user_id

        if album_id is not None:
            offset_diff = TRACKS_PER_ALBUM_PAGE
        else:
            offset_diff = TRACKS_PER_USER_PAGE

        offset = 0
        while True:
            response = self._vk.http.post(
                'https://m.vk.com/audio',
                data={
                    'act': 'load_section',
                    'owner_id': owner_id,
                    'playlist_id': album_id if album_id else -1,
                    'offset': offset,
                    'type': 'playlist',
                    'access_hash': access_hash,
                    'is_loading_all': 1
                },
                allow_redirects=False
            ).json()

            # print(response)

            if not response['data'][0]:
                raise Exception('Error get_iter')

            ids = scrap_ids(
                response['data'][0]['list']
            )
            if not ids:
                break

            tracks = scrap_tracks(
                ids,
                self.user_id,
                self._vk.http,
                convert_m3u8_links=self.convert_m3u8_links
            )

            for i in tracks:
                yield i

            if response['data'][0]['hasMore']:
                offset += offset_diff
            else:
                break


def scrap_ids(audio_data):
    """ Парсинг списка хэшей аудиозаписей из json объекта """
    ids = []

    for track in audio_data:
        audio_hashes = track[13].split("/")

        full_id = (
            str(track[1]), str(track[0]), audio_hashes[2], audio_hashes[5]
        )
        if all(full_id):
            ids.append(full_id)

    return ids


def scrap_tracks(ids, user_id, http, convert_m3u8_links=True):
    scrap_album(http)

    last_request = 0.0

    for ids_group in [ids[i:i + 10] for i in range(0, len(ids), 10)]:
        delay = RPS_DELAY_RELOAD_AUDIO - (time.time() - last_request)

        if delay > 0:
            time.sleep(delay)

        result = http.post(
            'https://m.vk.com/audio',
            data={'act': 'reload_audio', 'ids': ','.join(['_'.join(i) for i in ids_group])}
        ).json()

        last_request = time.time()
        if result['data']:
            data_audio = result['data'][0]
            for audio in data_audio:
                artist = BeautifulSoup(audio[4], 'html.parser').text
                title = BeautifulSoup(audio[3].strip(), 'html.parser').text
                duration = audio[5]
                link = audio[2]

                if 'audio_api_unavailable' in link:
                    link = decode_audio_url(link, user_id)

                if convert_m3u8_links and 'm3u8' in link:
                    link = RE_M3U8_TO_MP3.sub(r'\1/\2.mp3', link)

                yield {
                    'id': audio[0],
                    'owner_id': audio[1],
                    'track_covers': audio[14].split(',') if audio[14] else [],
                    'url': link,

                    'artist': artist,
                    'title': title,
                    'duration': duration,
                }


def scrap_album(http):
    url = 'https://vk.com/audio-2001167296_6167296?z=audio_playlist-2000218671_218671%2Fd840467901460d57e5'
    r = http.post(url)
    soup = BeautifulSoup(r.text, 'lxml')

    result = soup.find('div', class_='audio_row__info _audio_row__info')

    print(result)
