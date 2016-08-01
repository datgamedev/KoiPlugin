using UnityEngine;
using System.Collections;
using com.grepgame.android.plugin;
public class KoiCallBack : MonoBehaviour {

    

    // Use this for initialization
    void Awake () {
	
	}
	
	// Update is called once per frame
	void Update () {
	
	}

    void KoiCamera_PickDone(string param)
    {
        Koi.getInstance().handleImage(param);
    }

    void KoiGallery_PickDone(string param)
    {

        Koi.getInstance().handleImage(param);
    }

   
}
