import os

from bs4 import BeautifulSoup
from music_story import MusicStoryApi


def authMS():
    apiMS = MusicStoryApi('93823ea24d16e86b9c91ceb0967b8aadf2850f33', 'c0b0beae4b1c6742255d9ebab22e3b584accdc51')
    apiMS.connect()

    token = apiMS.token
    token_secret = apiMS.token_secret

    apiMS = MusicStoryApi('93823ea24d16e86b9c91ceb0967b8aadf2850f33', 'c0b0beae4b1c6742255d9ebab22e3b584accdc51', token,
                          token_secret)
    apiMS.connect()

    return apiMS


def scriptGetGenresOfNameArtist(name_artist):
    apiMusic = authMS()

    responseArtist = apiMusic.session.post(f"http://api.music-story.com/en/artist/search?name={name_artist}")
    soupArtist = BeautifulSoup(responseArtist.text, 'lxml')
    # может быть найдено несколько исполнителей
    idArtist = soupArtist.find('id').text
    # print(idArtist)

    responseGenresOfArtist = apiMusic.session.post(f"http://api.music-story.com/en/artist/{idArtist}/genres")
    soupGenres = BeautifulSoup(responseGenresOfArtist.text, 'xml')
    namesGenres = soupGenres.findAll('name')
    arrayGenres = []
    for i in namesGenres:
        arrayGenres.append(i.text)
    # print(arrayGenres)

    return arrayGenres
