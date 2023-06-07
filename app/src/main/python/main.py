from datetime import time

import requests
# import vk_api
# from bs4 import BeautifulSoup
# from music_story import MusicStoryApi
# from vk_api import audio
# from tqdm import tqdm

import Audio

# myId = 237598125
# Stas = 145558731
# vk_session = vk_api.VkApi('+79048929847', 'Qwertyrobot_2018@')
# vk_session.auth()
#
# api = MusicStoryApi('93823ea24d16e86b9c91ceb0967b8aadf2850f33', 'c0b0beae4b1c6742255d9ebab22e3b584accdc51')
# api.connect()
#
# token = api.token
# token_secret = api.token_secret
#
# api = MusicStoryApi('93823ea24d16e86b9c91ceb0967b8aadf2850f33', 'c0b0beae4b1c6742255d9ebab22e3b584accdc51', token, token_secret)
# api.connect()

#беру из vkApi
#!python2
import pythonScript

nameArtist = "My Darkest Days"
pir = "pyrokinesis"
# pythonScript.scriptGetGenresOfNameArtist(pir)
# responseArtist = api.session.post(f"http://api.music-story.com/en/artist/search?name={nameArtist}")
# soupArtist = BeautifulSoup(responseArtist.text, 'xml')
# #может быть найдено несколько исполнителей
# idArtist = soupArtist.find('id').text
# print(idArtist)
#
# responseGenresOfArtist = api.session.post(f"http://api.music-story.com/en/artist/{idArtist}/genres")
# soupGenres = BeautifulSoup(responseGenresOfArtist.text, 'xml')
# namesGanres = soupGenres.findAll('name')
# arrayGenres = []
# for i in namesGanres:
#     arrayGenres.append(i.text)
# print(arrayGenres)
# responseGenre =

# genre = api.get("genre", 66)
# print(genre.name)
#
# artist = api.get("artist")
# print(artist)
#
# genre = api.get('genre', 64)
# artistes = genre.connector('artists', name='Jon')
# print(artistes[0].name)

# vk = vk_session.get_api()

# vk_audio = audio.VkAudio(vk_session)

# vk_audio_me = Audio.VkAudio(vk_session)
#
# vk_audio_me.get(myId)
# song = 0
# for i in vk_audio_me.get(myId):
#     try:
#         song += 1
#         r = requests.get(i['url'], True)
#         size = int(r.headers['Content-Length'])
#         if r.status_code == 200:
#             print(i['artist'] + ' - ' + i['title'])
#     except:
#         print('error')
