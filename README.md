 ## Switcher-Library
 A simple library for creating customized switch
 

![alt text](https://github.com/Sva95/Switcher-Library/blob/master/gif/switcher.gif)
 
## Benchmark
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)

## Mentions
[![](https://jitpack.io/v/Sva95/Switcher-Library.svg)](https://jitpack.io/#Sva95/Switcher-Library)
 
## Installation

```
maven { url 'https://jitpack.io' }

dependencies {
  implementation 'com.github.Sva95:Switcher-Library:1.0.2'
}
```

## Usage

#### XML
Add the following lines in your xml
 ```
<com.fx.switcher.Switcher
        android:id="@+id/fxSwither"
        android:layout_width="60dp"
        android:layout_height="30dp"
        app:switchBackgroundColor="@color/colorPrimary"
        app:switchState="true" />
 ```
 #### Java (Programatically)
 Create the switch programatically in your Fragment/Activity class
 ```
 Switcher switcher = new Switcher(this);
 ```
 #### Attrs
  
| attr                        |  for                    | 
| :---                        |  :---                   | 
| app:switchBackgroundColor   | custom background color |
| app:switchState             | set state status        |

#### Listener

This callback will provide the current state of the switch
```  
switcher.setOnClickSwitch(new Switcher.OnClickSwitch() {
            @Override
            public void switchOn() {
            
            }

            @Override
            public void switchOff() {

            }
        });
```  
## License

```
Copyright 2019 Sva

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

