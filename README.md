# react-native-mime-intent

Pass mimetypes to Android Intent from React Native

## Installation (Android)

* `android/settings.gradle`

```gradle
...
include ':react-native-mime-intent'
project(':react-native-mime-intent').projectDir = new File(settingsDir, '../node_modules/react-native-mime-intent/android')
```

* `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':react-native-mime-intent')
}
```

* register module (in `MainApplication.java`)

```java
import com.mimeintent.MimeIntentPackage; // <------- add package
// ...
public class MainApplication extends ReactActivity {
   // ...
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(), // <---- add comma
        new MimeIntentPackage() // <---------- add package
      );
    }
```

## Usage

```javascript
var MimeIntent = require('react-native-mime-intent');
if (MimeIntent.canOpenURLWithMime('file:///data/file.mp3', 'audio/*')) {
    MimeIntent.openURLWithMime('file:///data/file.mp3', 'audio/*');
}
```
