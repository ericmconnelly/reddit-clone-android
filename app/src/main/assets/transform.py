import json

# https://www.reddit.com/r/aww/hot.json?limit=100
# https://www.reddit.com/subreddits/popular.json?limit=100 

img_index=0
imgs = ['file:///android_asset/bigcat0.jpg',
        'file:///android_asset/bigcat1.jpg',
        'file:///android_asset/bigcat2.jpg',
        'file:///android_asset/bigdog0.jpg']
with open('aww.hot.1.100.json.txt') as json_file:
    data = json.load(json_file)
    d_arr = data['data']['children']
    for d in d_arr:
        d['data']['thumbnail'] = imgs[img_index]
        d['data']['url'] = imgs[img_index]
        img_index += 1
        if img_index >= 4:
            img_index = 0
    with open('aww.hot.1.100.json.transformed.txt', 'w') as outfile:
        json.dump(data, outfile)
