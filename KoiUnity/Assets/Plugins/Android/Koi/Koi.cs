using System;
using UnityEngine;


namespace com.grepgame.android.plugin
{
    public class Koi
    {

        public delegate void ImageDataReceived(string message, byte[] imageData);
        public ImageDataReceived imageDelegate;

#if (UNITY_ANDROID && !UNITY_EDITOR)
        AndroidJavaObject androidObject;
        AndroidJavaObject unityObject;
#endif

        private static Koi instance;

        public void handleImage(string param)
        {
            if (imageDelegate == null)
            {
                Debug.Log("You must assign imageDelegate first");
            }
            else
            {
#if (UNITY_ANDROID && !UNITY_EDITOR)
                imageDelegate(param, androidObject.Call<byte[]>("getPluginData"));
                cleanUp();
#endif
            }

        }

        public static Koi getInstance()
        {

            if (instance == null)
            {

                instance = new Koi();

            }

            return instance;



        }

        private Koi()
        {

#if (UNITY_ANDROID && !UNITY_EDITOR)

            AndroidJavaClass unityClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            unityObject = unityClass.GetStatic<AndroidJavaObject>("currentActivity");

            AndroidJavaClass androidClass = new AndroidJavaClass("com.grepgame.android.plugin.grplugin.Koi");
            androidObject = androidClass.GetStatic<AndroidJavaObject>("instance");
#endif

        }

        public void openCamera()
        {
#if (UNITY_ANDROID && !UNITY_EDITOR)
            androidObject.Call("launchCamera", unityObject);
#endif
        }

        public void openGallery()
        {
#if (UNITY_ANDROID && !UNITY_EDITOR)
            androidObject.Call("launchGallery", unityObject);
#endif
        }



        public void cleanUp()
        {
#if (UNITY_ANDROID && !UNITY_EDITOR)
            androidObject.Call("cleanUp");
#endif
        }

    }
    }