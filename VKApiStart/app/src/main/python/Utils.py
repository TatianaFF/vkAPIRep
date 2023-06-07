from http.cookiejar import Cookie


def cookie_from_dict(d):
    return Cookie(**d)


def set_cookies_from_list(cookie_jar, l):
    for cookie in l:
        cookie_jar.set_cookie(cookie_from_dict(cookie))